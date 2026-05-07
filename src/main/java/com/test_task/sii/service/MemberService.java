package com.test_task.sii.service;

import com.test_task.sii.dto.MemberDTO;
import com.test_task.sii.entity.Member;
import com.test_task.sii.entity.MemberStatus;
import com.test_task.sii.entity.MembershipPlan;
import com.test_task.sii.repository.MemberRepository;
import com.test_task.sii.repository.MembershipPlanRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MembershipPlanRepository membershipPlanRepository;
    private final MemberRepository memberRepository;
    private final MembershipPlanService membershipPlanService;

    public MemberService(MembershipPlanRepository membershipPlanRepository,
                         MemberRepository memberRepository,
                         MembershipPlanService membershipPlanService) {
        this.membershipPlanRepository = membershipPlanRepository;
        this.memberRepository = memberRepository;
        this.membershipPlanService = membershipPlanService;
    }

    @Transactional
    public MemberDTO saveNewMember(MemberDTO memberDTO, Long gymId, Long planId){
        MembershipPlan plan = membershipPlanRepository.findById(planId).orElseThrow(()->
                new EntityNotFoundException("Plan with ID: " + planId + " is not found."));
        if (!plan.getGym().getId().equals(gymId)) {
            throw new IllegalArgumentException("Plan with ID: " + planId + " does not belong to Gym with ID: " + gymId);
        }
        Long count = memberRepository.countByMembershipPlanIdAndStatus(planId, MemberStatus.ACTIVE);
        if (count >= plan.getMaxMembers()){
            throw new IllegalArgumentException("Plan with ID: " + planId + " has max number of members");
        }
        Member newMember = convertDTOtoEntity(memberDTO);
        newMember.setMembershipPlan(plan);
        Member response = memberRepository.save(newMember);
        return convertEntityToDTO(response);
    }

    private Member convertDTOtoEntity(MemberDTO memberDTO) {
        Member member = new Member();
        member.setFullname(memberDTO.getFullname());
        member.setEmail(memberDTO.getEmail());
        return member;
    }

    private MemberDTO convertEntityToDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setFullname(member.getFullname());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setStatus(member.getStatus());
        memberDTO.setStartDate(member.getStartDate());

        memberDTO.setMembershipPlanDTO(membershipPlanService.convertEntityToDTO(member.getMembershipPlan()));

        return memberDTO;
    }
}
