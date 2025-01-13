package com.bettercloud.secret_santa.controller;

import com.bettercloud.secret_santa.dto.FamilyId;
import com.bettercloud.secret_santa.dto.FamilyList;
import com.bettercloud.secret_santa.model.FamilyMember;
import com.bettercloud.secret_santa.service.FamilyMemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("secretSanta")
public class SecretSantaController {

    @Autowired
    FamilyMemberService familyMemberService;

    @PostMapping("/family")
    public ResponseEntity<?> createFamilyList (@Valid @RequestBody FamilyList familyList){
        FamilyList famListResponse = familyMemberService.createFamily(familyList);
        if (famListResponse == null){
            return new ResponseEntity<>("No family created", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(famListResponse, HttpStatus.CREATED);
    }

    @GetMapping("/family/{id}")
    public ResponseEntity<?> getFamilyMembers (@PathVariable String id){

        List<FamilyMember> familyMembers = familyMemberService.getFamily(id);
        FamilyList familyList = new FamilyList(new FamilyId(id), familyMembers);
        if (familyMembers == null || familyMembers.size() == 0){
            return new ResponseEntity<>("No family found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(familyList, HttpStatus.OK);
    }

    @PutMapping("/family/{id}")
    public ResponseEntity<?> updateFamilyList (@PathVariable String id,
                                               @Valid @RequestBody List<FamilyMember> familyMembers){
        List<FamilyMember> famMembersResponse = familyMemberService.updateFamilyMembers(id, familyMembers);
        if (famMembersResponse == null || famMembersResponse.size() == 0){
            return new ResponseEntity<>("No family updated", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(famMembersResponse, HttpStatus.OK);
    }

    @PostMapping("/family/assignments")
    public ResponseEntity<?> assignSecretSanta (@Valid @RequestBody FamilyId familyId){

        Map<String, String> assignments = familyMemberService.assignSecretSanta(familyId);
        if (assignments == null || assignments.size() == 0){
            return new ResponseEntity<>("No assignments were done", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(assignments, HttpStatus.OK);
    }
}
