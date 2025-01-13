package com.bettercloud.secret_santa.service;

import com.bettercloud.secret_santa.dto.FamilyId;
import com.bettercloud.secret_santa.dto.FamilyList;
import com.bettercloud.secret_santa.model.FamilyMember;
import com.bettercloud.secret_santa.repository.FamilyMemberRepository;
import com.bettercloud.secret_santa.repository.SecretSantaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Service
public class FamilyMemberServiceImpl implements FamilyMemberService {

    @Autowired
    FamilyMemberRepository familyMemberRepository;

    @Autowired
    SecretSantaRepository secretSantaRepository;

    @Override
    public FamilyList createFamily(FamilyList familyList) {
        return familyMemberRepository.save(familyList);
    }

    @Override
    public List<FamilyMember> getFamily(String id) {
        return familyMemberRepository.find(id);
    }

    @Override
    public List<FamilyMember> updateFamilyMembers(String id, List<FamilyMember> familyMembers) {
        return familyMemberRepository.update(id, familyMembers);
    }

    @Override
    public Map<String, String> assignSecretSanta(FamilyId familyId) {
        List<String> participants = new ArrayList<>();
        List<FamilyMember> familyMembers = familyMemberRepository.find(familyId.getId());
        familyMembers.forEach(member -> participants.add(member.getName()));

        Collections.shuffle(participants); // Shuffle the list for random assignment
        Map<String, String> assignments = new HashMap<>();
        Set<String> assigned = new HashSet<>();

        for (String giver : participants) {
            String receiver = findReceiver(familyId.getId(), giver, participants, assigned, familyMembers);
            assignments.put(giver, receiver);
            assigned.add(receiver);
        }

        return assignments;
    }

    private String findReceiver(String familyId, String giver, List<String> participants, Set<String> assigned,
                                       List<FamilyMember> familyMembers) {
        Random random = new Random();
        String receiver;
        List<String> eligibleReceivers = new ArrayList<>();

        // Identify eligible receivers based on immediate family and already assigned users
        for (String participant : participants) {
            // Check if the participant is not the giver, not already assigned, and not an immediate family member
            //and not previously assigned in the past 3 years
            if (!participant.equals(giver) && !assigned.contains(participant)
                    && !isImmediateFamily(giver, participant, familyMembers)
                    && !isPreviouslyAssigned(giver, participant, familyId)) {
                eligibleReceivers.add(participant);
            }
        }

        // If there's at least one eligible receiver, select one randomly
        if (!eligibleReceivers.isEmpty()) {
            receiver = eligibleReceivers.get(random.nextInt(eligibleReceivers.size()));
        } else {
            // In case no eligible receivers left, retry assignment logic (this case should be rare)
            receiver = participants.get(random.nextInt(participants.size()));
        }

        //save the current assignation
        secretSantaRepository.save(familyId, giver, receiver);

        return receiver;
    }

    private boolean isImmediateFamily(String giver, String potentialReceiver, List<FamilyMember> familyMembers) {
        for (FamilyMember member : familyMembers) {
            if (member.getName().equals(giver)) {
                return member.getImmediateFamily().contains(potentialReceiver);
            }
        }
        return false;
    }

    private boolean isPreviouslyAssigned(String giver, String potentialReceiver, String familyId) {
        List<String> previouslyAssigned = secretSantaRepository.fetchPreviousAssignments(familyId,giver);
        return previouslyAssigned.contains(potentialReceiver);
    }
}
