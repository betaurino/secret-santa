package com.bettercloud.secret_santa.repository;

import java.util.List;

public interface SecretSantaRepository {

    List<String> save(String familyId, String giver ,String receiver);

    List<String> fetchPreviousAssignments(String familyId, String name);
}
