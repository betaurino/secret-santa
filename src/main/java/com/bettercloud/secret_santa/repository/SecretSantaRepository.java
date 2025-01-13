package com.bettercloud.secret_santa.repository;

import com.bettercloud.secret_santa.dto.FamilyList;
import com.bettercloud.secret_santa.model.FamilyMember;

import java.util.List;
import java.util.Map;

public interface SecretSantaRepository {

    List<String> save(String familyId, String giver ,String receiver);

    List<String> fetchPreviousAssignments(String familyId, String name);
}
