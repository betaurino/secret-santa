package com.bettercloud.secret_santa.service;

import com.bettercloud.secret_santa.dto.FamilyId;
import com.bettercloud.secret_santa.dto.FamilyList;
import com.bettercloud.secret_santa.model.FamilyMember;

import java.util.List;
import java.util.Map;

public interface FamilyMemberService {
    FamilyList createFamily(FamilyList familyList);

    List<FamilyMember> getFamily(String id);

    List<FamilyMember> updateFamilyMembers(String id, List<FamilyMember> familyMembers);

    Map<String, String> assignSecretSanta(FamilyId familyId);
}
