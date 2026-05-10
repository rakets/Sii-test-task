package com.test_task.sii.service;

import com.test_task.sii.dto.GymDTO;
import com.test_task.sii.dto.MembershipPlanDTO;
import com.test_task.sii.entity.Gym;
import com.test_task.sii.entity.MembershipPlan;
import com.test_task.sii.entity.PlanType;
import com.test_task.sii.repository.GymRepository;
import com.test_task.sii.repository.MembershipPlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MembershipPlanServiceTest {
    @Mock
    private MembershipPlanRepository membershipPlanRepository;
    @Mock
    private GymRepository gymRepository;
    @Mock
    private GymService gymService;

    @InjectMocks
    private MembershipPlanService membershipPlanService;

    MembershipPlanDTO createMembershipPlanDTO() {
        MembershipPlanDTO planDTO = new MembershipPlanDTO();
        planDTO.setName("Lux");
        planDTO.setType(PlanType.BASIC);
        planDTO.setMonthlyPrice(BigDecimal.valueOf(1200));
        planDTO.setCurrency("USD");
        planDTO.setDurationMonths(3);
        planDTO.setMaxMembers(3);
        return planDTO;
    }

    MembershipPlan createMembershipPlan(Gym gym) {
        MembershipPlan plan = new MembershipPlan();
        plan.setId(1L);
        plan.setName("Lux");
        plan.setType(PlanType.BASIC);
        plan.setMonthlyPrice(BigDecimal.valueOf(1200));
        plan.setCurrency(Currency.getInstance("USD"));
        plan.setDurationMonths(3);
        plan.setMaxMembers(3);
        plan.setGym(gym);
        return plan;
    }

    Gym createGym() {
        Gym gym = new Gym();
        gym.setId(1L);
        gym.setName("Lublin Shark");
        gym.setAddress("Kiepury 14");
        gym.setPhoneNumber("+48511222333");
        return gym;
    }

    GymDTO createGymDTO() {
        GymDTO gymDTO = new GymDTO();
        gymDTO.setName("Lublin Shark");
        gymDTO.setAddress("Kiepury 14");
        gymDTO.setPhoneNumber("+48511222333");
        return gymDTO;
    }

    @Test
    void saveNewMembershipPlan_ShouldSaveAndReturnMembershipPlanDTO() {
        // GIVEN
        MembershipPlanDTO planDTO = createMembershipPlanDTO();
        Long gymId = 1L;

        Gym gym = createGym();
        gym.setId(gymId);

        GymDTO gymDTO = createGymDTO();

        MembershipPlan plan = createMembershipPlan(gym);

        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));
        when(membershipPlanRepository.existsByNameAndGym_Id(planDTO.getName(), gymId)).thenReturn(false);
        when(membershipPlanRepository.save(any(MembershipPlan.class))).thenReturn(plan);
        when(gymService.convertEntityToDTO(plan.getGym())).thenReturn(gymDTO);
        // WHEN
        MembershipPlanDTO response = membershipPlanService.saveNewMembershipPlan(planDTO, gymId);
        // THEN
        assertNotNull(response);
        assertEquals(planDTO.getName(), response.getName());
        assertEquals(planDTO.getType(), response.getType());
        assertEquals(planDTO.getMonthlyPrice(), response.getMonthlyPrice());
        assertEquals(planDTO.getCurrency(), response.getCurrency());
        assertEquals(planDTO.getDurationMonths(), response.getDurationMonths());
        assertEquals(planDTO.getMaxMembers(), response.getMaxMembers());
        verify(gymRepository, times(1)).findById(gymId);
        verify(membershipPlanRepository, times(1)).existsByNameAndGym_Id(planDTO.getName(), gymId);
        verify(membershipPlanRepository, times(1)).save(any(MembershipPlan.class));
    }
}
