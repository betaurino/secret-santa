package com.bettercloud.secret_santa.dto;

import com.bettercloud.secret_santa.model.FamilyMember;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FamilyList {

    @NotNull
    private FamilyId familyId;
    @NotEmpty
    private List<FamilyMember> familyMembers;

}
