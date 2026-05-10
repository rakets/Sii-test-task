package com.test_task.sii.service;

import com.test_task.sii.dto.GymDTO;
import com.test_task.sii.dto.MembershipPlanDTO;
import com.test_task.sii.entity.Gym;
import com.test_task.sii.entity.MembershipPlan;
import com.test_task.sii.entity.PlanType;
import com.test_task.sii.repository.GymRepository;
import com.test_task.sii.repository.MembershipPlanRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
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

    // test for success method 'saveNewMembershipPlan'
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

    // test for exception IllegalArgumentException in method 'saveNewMembershipPlan' when invalid currency code
    @Test
    void saveNewMembershipPlan_ShouldThrowIllegalArgumentException() {
        // GIVEN
        MembershipPlanDTO planDTO = createMembershipPlanDTO();
        planDTO.setCurrency("ust");
        Long gymId = 1L;
        // WHEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> membershipPlanService.saveNewMembershipPlan(planDTO, gymId));
        // THEN
        assertEquals("Invalid currency code", exception.getMessage());
        verify(gymRepository, never()).findById(gymId);
        verify(membershipPlanRepository, never()).existsByNameAndGym_Id(planDTO.getName(), gymId);
        verify(membershipPlanRepository, never()).save(any(MembershipPlan.class));
    }

    // test for exception EntityNotFoundException in method 'saveNewMembershipPlan' when gym is not found
    @Test
    void saveNewMembershipPlan_ShouldThrowEntityNotFoundException() {
        // GIVEN
        MembershipPlanDTO planDTO = createMembershipPlanDTO();
        Long gymId = 1L;

        when(gymRepository.findById(gymId)).thenReturn(Optional.empty());
        // WHEN
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> membershipPlanService.saveNewMembershipPlan(planDTO, gymId));
        // THEN
        assertEquals("Gym with ID: " + gymId + " is not found", exception.getMessage());
        verify(gymRepository, times(1)).findById(gymId);
        verify(membershipPlanRepository, never()).existsByNameAndGym_Id(planDTO.getName(), gymId);
        verify(membershipPlanRepository, never()).save(any(MembershipPlan.class));
    }

    // test for exception IllegalArgumentException in method 'saveNewMembershipPlan' when gym has already plan with same name
    @Test
    void saveNewMembershipPlan_PlanWithSameName_ShouldThrowIllegalArgumentException() {
        // GIVEN
        MembershipPlanDTO planDTO = createMembershipPlanDTO();
        Long gymId = 1L;

        Gym gym = createGym();
        gym.setId(gymId);

        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));
        when(membershipPlanRepository.existsByNameAndGym_Id(planDTO.getName(), gymId)).thenReturn(true);
        // WHEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> membershipPlanService.saveNewMembershipPlan(planDTO, gymId));
        // THEN
        assertEquals("Gym has already plan with same name", exception.getMessage());
        verify(gymRepository, times(1)).findById(gymId);
        verify(membershipPlanRepository, times(1)).existsByNameAndGym_Id(planDTO.getName(), gymId);
        verify(membershipPlanRepository, never()).save(any(MembershipPlan.class));
    }

    // test for success method 'getAllPlansForGym'
    @Test
    void getAllPlansForGym_ShouldReturnListOfMembershipPlanDTO() {
        // GIVEN
        Long gymId = 1L;

        Gym gym = createGym();
        gym.setId(gymId);

        MembershipPlan plan = createMembershipPlan(gym);
        List<MembershipPlan> membershipPlanList = List.of(plan);

        GymDTO gymDTO = createGymDTO();

        when(gymRepository.existsById(gymId)).thenReturn(true);
        when(membershipPlanRepository.findByGymId(gymId)).thenReturn(membershipPlanList);
        when(gymService.convertEntityToDTO(gym)).thenReturn(gymDTO); // Нужно для маппинга внутри convertEntityToDTO

        // WHEN
        List<MembershipPlanDTO> response = membershipPlanService.getAllPlansForGym(gymId);

        // THEN
        assertNotNull(response);
        assertEquals(1, response.size()); // Проверяем, что размер списка совпадает

        MembershipPlanDTO responseDTO = response.get(0);
        assertEquals(plan.getName(), responseDTO.getName());
        assertEquals(plan.getType(), responseDTO.getType());
        assertEquals(plan.getMonthlyPrice(), responseDTO.getMonthlyPrice());
        assertEquals(plan.getCurrency().getCurrencyCode(), responseDTO.getCurrency());
        assertEquals(plan.getDurationMonths(), responseDTO.getDurationMonths());
        assertEquals(plan.getMaxMembers(), responseDTO.getMaxMembers());
        assertNotNull(responseDTO.getGymDTO());

        verify(gymRepository, times(1)).existsById(gymId);
        verify(membershipPlanRepository, times(1)).findByGymId(gymId);
        verify(gymService, times(1)).convertEntityToDTO(gym);
    }

    // test for exception EntityNotFoundException in method 'getAllPlansForGym' when gym is not found
    @Test
    void getAllPlansForGym_ShouldThrowEntityNotFoundException() {
        // GIVEN
        Long gymId = 1L;

        when(gymRepository.existsById(gymId)).thenReturn(false);

        // WHEN
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> membershipPlanService.getAllPlansForGym(gymId));
        // THEN
        assertEquals("Gym with ID: " + gymId + " is not found", exception.getMessage());
        verify(gymRepository, times(1)).existsById(gymId);
        verify(membershipPlanRepository, never()).findByGymId(anyLong());
        verify(gymService, never()).convertEntityToDTO(any(Gym.class));
    }

    // test for success method 'convertEntityToDTO'
    @Test
    void convertEntityToDTO_ShouldReturnMembershipPlanDTO() {
        // GIVEN
        Gym gym = createGym();
        GymDTO gymDTO = createGymDTO();
        MembershipPlan plan = createMembershipPlan(gym);
        when(gymService.convertEntityToDTO(any(Gym.class))).thenReturn(gymDTO);

        // WHEN
        MembershipPlanDTO response = membershipPlanService.convertEntityToDTO(plan);

        // THEN
        assertNotNull(response);
        assertEquals(plan.getId(), response.getId());
        assertEquals(plan.getName(), response.getName());
        assertEquals(plan.getType(), response.getType());
        assertEquals(plan.getMonthlyPrice(), response.getMonthlyPrice());
        assertEquals(plan.getCurrency().getCurrencyCode(), response.getCurrency());
        assertEquals(plan.getDurationMonths(), response.getDurationMonths());
        assertEquals(plan.getMaxMembers(), response.getMaxMembers());

        assertNotNull(response.getGymDTO());
        assertEquals(gymDTO.getName(), response.getGymDTO().getName());

        verify(gymService, times(1)).convertEntityToDTO(any(Gym.class));
    }

    // test for success method 'convertDTOtoEntity'
    @Test
    void convertDTOtoEntity_ShouldReturnMembershipPlan() {
        // GIVEN
        MembershipPlanDTO planDTO = createMembershipPlanDTO();
        // WHEN
        MembershipPlan response = membershipPlanService.convertDTOtoEntity(planDTO);
        // THEN
        assertNotNull(response);
        assertEquals(planDTO.getName(), response.getName());
        assertEquals(planDTO.getType(), response.getType());
        assertEquals(planDTO.getMonthlyPrice(), response.getMonthlyPrice());
        assertEquals(planDTO.getCurrency(), response.getCurrency().getCurrencyCode());
        assertEquals(planDTO.getDurationMonths(), response.getDurationMonths());
        assertEquals(planDTO.getMaxMembers(), response.getMaxMembers());
    }
}
