package com.test_task.sii.service;

import com.test_task.sii.dto.GymDTO;
import com.test_task.sii.dto.MembershipPlanDTO;
import com.test_task.sii.entity.Gym;
import com.test_task.sii.entity.MembershipPlan;
import com.test_task.sii.repository.GymRepository;
import com.test_task.sii.repository.MembershipPlanRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MembershipPlanService {
    private final MembershipPlanRepository membershipPlanRepository;
    private final GymRepository gymRepository;
    private final GymService gymService;

    public MembershipPlanService(MembershipPlanRepository membershipPlanRepository, GymRepository gymRepository, GymService gymService) {
        this.membershipPlanRepository = membershipPlanRepository;
        this.gymRepository = gymRepository;
        this.gymService = gymService;
    }

    @Transactional
    public MembershipPlanDTO saveNewMembershipPlan(MembershipPlanDTO membershipPlanDTO, Long gymId) {
        Gym currentGym = gymRepository.findById(gymId).orElseThrow(() ->
                new EntityNotFoundException("Gym with ID: " + gymId + " is not found"));
        if (membershipPlanRepository.existsByNameAndGym_Id(membershipPlanDTO.getName(), currentGym.getId())) {
            throw new IllegalArgumentException("Gym has already plan with same name");
        }
        MembershipPlan newPlan = convertDTOtoEntity(membershipPlanDTO);
        newPlan.setGym(currentGym);
        MembershipPlan savedPlan = membershipPlanRepository.save(newPlan);
        return convertEntityToDTO(savedPlan);
    }

    public List<MembershipPlanDTO> getAllPlansForGym(Long gymId) {
        if (!gymRepository.existsById(gymId)) {
            throw new EntityNotFoundException("Gym with ID: " + gymId + " is not found");
        }
        List<MembershipPlan> membershipPlanList = membershipPlanRepository.findByGymId(gymId);
        List<MembershipPlanDTO> membershipPlanDTOList = new ArrayList<>();
        for (MembershipPlan plan : membershipPlanList){
            membershipPlanDTOList.add(convertEntityToDTO(plan));
        }
        return membershipPlanDTOList;
    }

    public MembershipPlanDTO convertEntityToDTO(MembershipPlan membershipPlan){
        MembershipPlanDTO planDTO = new MembershipPlanDTO();
        planDTO.setId(membershipPlan.getId());
        planDTO.setName(membershipPlan.getName());
        planDTO.setType(membershipPlan.getType());
        planDTO.setMonthlyPrice(membershipPlan.getMonthlyPrice());
        planDTO.setCurrency(membershipPlan.getCurrency());
        planDTO.setDurationMonths(membershipPlan.getDurationMonths());
        planDTO.setMaxMembers(membershipPlan.getMaxMembers());

        planDTO.setGymDTO(gymService.convertEntityToDTO(membershipPlan.getGym()));

        return planDTO;
    }

    private MembershipPlan convertDTOtoEntity(MembershipPlanDTO membershipPlanDTO){
        MembershipPlan planEntity = new MembershipPlan();
        planEntity.setName(membershipPlanDTO.getName());
        planEntity.setType(membershipPlanDTO.getType());
        planEntity.setMonthlyPrice(membershipPlanDTO.getMonthlyPrice());
        planEntity.setCurrency(membershipPlanDTO.getCurrency());
        planEntity.setDurationMonths(membershipPlanDTO.getDurationMonths());
        planEntity.setMaxMembers(membershipPlanDTO.getMaxMembers());
        return planEntity;
    }
}
