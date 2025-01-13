package com.bettercloud.secret_santa.repository;

import com.bettercloud.secret_santa.dto.FamilyList;
import com.bettercloud.secret_santa.model.FamilyMember;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class FamilyMemberRepositoryImpl implements FamilyMemberRepository {

    private static Logger logger = LoggerFactory.getLogger(FamilyMemberRepositoryImpl.class);

    private static final String TEXT_FILENAME = "../families.json";
    private final Map<String, Map<String, Set<String>>> repository;

    public FamilyMemberRepositoryImpl() {
        this.repository = new HashMap<>();
    }

    @Override
    public FamilyList save(FamilyList familyList) {
        Map<String, Set<String>> result = familyList.getFamilyMembers().stream().collect(
                Collectors.toMap(FamilyMember::getName, FamilyMember::getImmediateFamily));
        repository.put(familyList.getFamilyId().getId(), result);
        return familyList;
    }

    @Override
    public List<FamilyMember> find(String id) {
        repository.computeIfAbsent(id, k -> new HashMap<>());
        Map<String, Set<String>> familyMembers = repository.get(id);
        List<FamilyMember> familyMembersList = new ArrayList<>();
        familyMembers.forEach((k, v) -> familyMembersList.add(new FamilyMember(k, v)));
        return familyMembersList;
    }

    @Override
    public List<FamilyMember> update(String id, List<FamilyMember> familyMembers) {
        Map<String, Set<String>> result = familyMembers.stream().collect(
                Collectors.toMap(FamilyMember::getName, FamilyMember::getImmediateFamily));
        repository.put(id, result);
        return familyMembers;
    }

    @PostConstruct
    private void postConstruct() {
        try {
            Path path = Paths.get(TEXT_FILENAME);
            if (Files.exists(path)) {
                ObjectMapper mapper = new ObjectMapper();
                repository.putAll(mapper.readValue(new File(TEXT_FILENAME),
                        new TypeReference<Map<String, Map<String, Set<String>>>>() {
                        }));
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    @PreDestroy
    public void preDestroy() {
        try {
            Path filePath = Paths.get(TEXT_FILENAME);
            Files.deleteIfExists(filePath);
            Files.createFile(filePath);
            ObjectMapper mapper = new ObjectMapper();
            String jsonRepo = mapper.writeValueAsString(repository);
            Files.writeString(filePath, jsonRepo, StandardOpenOption.WRITE);
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
}
