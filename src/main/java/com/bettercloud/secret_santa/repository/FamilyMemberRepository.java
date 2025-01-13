package com.bettercloud.secret_santa.repository;

import com.bettercloud.secret_santa.dto.FamilyList;
import com.bettercloud.secret_santa.model.FamilyMember;

import java.util.List;

public interface FamilyMemberRepository  {

    FamilyList save(FamilyList familyList);

    List<FamilyMember> update(String id, List<FamilyMember> familyMembers);

    List<FamilyMember> find(String id);
}
