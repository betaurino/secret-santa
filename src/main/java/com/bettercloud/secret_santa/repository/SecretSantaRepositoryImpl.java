package com.bettercloud.secret_santa.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SecretSantaRepositoryImpl implements SecretSantaRepository {

    private static Logger logger = LoggerFactory.getLogger(SecretSantaRepositoryImpl.class);

    private static final String TEXT_FILENAME = "../secretsanta.json";
    private final Map<String, Map <String, List<String>>> repository;

    public SecretSantaRepositoryImpl() {
        this.repository = new HashMap<>();
    }

    @Override
    public List<String> save(String familyId, String giver ,String receiver) {
        repository.computeIfAbsent(familyId, k -> new HashMap<>());
        repository.get(familyId).computeIfAbsent(giver, k -> new ArrayList<>(3));

        List<String> previousAssignations = repository.get(familyId).get(giver);

        if (previousAssignations.size() < 3){
            previousAssignations.add(receiver);
        }
        else {
            previousAssignations.remove(0);
            previousAssignations.add(receiver);
        }
        return previousAssignations;
    }

    @Override
    public List<String> fetchPreviousAssignments(String familyId, String name) {
        repository.computeIfAbsent(familyId, k -> new HashMap<>());
        repository.get(familyId).computeIfAbsent(name, k -> new ArrayList<>(3));
        return repository.get(familyId).get(name);
    }

    @PostConstruct
    private void postConstruct() {
        try {
            Path path = Paths.get(TEXT_FILENAME);
            if (Files.exists(path)) {
                ObjectMapper mapper = new ObjectMapper();
                repository.putAll(mapper.readValue(new File(TEXT_FILENAME),
                        new TypeReference<Map<String, Map <String, List<String>>>>(){}));
            }
        } catch (Exception e){
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
        } catch (Exception e){
            logger.error(e.toString());
        }
    }
}
