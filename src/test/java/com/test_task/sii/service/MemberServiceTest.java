package com.test_task.sii.service;

import com.test_task.sii.dto.MemberDTO;
import com.test_task.sii.dto.MembershipPlanDTO;
import com.test_task.sii.entity.*;
import com.test_task.sii.repository.MemberRepository;
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
public class MemberServiceTest {
    @Mock
    private MembershipPlanRepository membershipPlanRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MembershipPlanService membershipPlanService;

    @InjectMocks
    private MemberService memberService;

    Gym createGym() {
        Gym gym = new Gym();
        gym.setName("Lublin Shark");
        gym.setAddress("Kiepury 14");
        gym.setPhoneNumber("+48511222333");
        return gym;
    }

    MembershipPlan createMembershipPlan(Gym gym) {
        MembershipPlan plan = new MembershipPlan();
        plan.setName("Lux");
        plan.setType(PlanType.PREMIUM);
        plan.setMonthlyPrice(BigDecimal.valueOf(150.50));
        plan.setCurrency(Currency.getInstance("USD"));
        plan.setDurationMonths(4);
        plan.setMaxMembers(3);
        plan.setGym(gym);
        return plan;
    }

    MembershipPlanDTO createMembershipPlanDTO() {
        MembershipPlanDTO membershipPlanDTO = new MembershipPlanDTO();
        membershipPlanDTO.setName("Lux");
        return membershipPlanDTO;
    }

    MemberDTO createMemberDTO() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setFullname("Valiantsin Ushkevich");
        memberDTO.setEmail("vushkevih@gmail.com");
        return memberDTO;
    }

    Member createMember() {
        Member member = new Member();
        member.setId(1L);
        member.setFullname("Valiantsin Ushkevich");
        member.setEmail("vushkevih@gmail.com");
        member.setStatus(MemberStatus.ACTIVE);
        return member;
    }

    // test for success method 'saveNewMember'
    @Test
    void saveNewMember_ShouldSaveNewMemberAndReturnMemberDTO() {
        // GIVEN
        Long gymId = 1L;
        Long planId = 1L;

        Long count = 1L;

        Gym gym = createGym();
        gym.setId(gymId);

        MembershipPlan plan = createMembershipPlan(gym);
        MembershipPlanDTO planDTO = createMembershipPlanDTO();

        MemberDTO memberDTO = createMemberDTO();

        Member member = createMember();
        member.setMembershipPlan(plan);

        when(membershipPlanRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(memberRepository.countByMembershipPlanIdAndStatus(planId, MemberStatus.ACTIVE)).thenReturn(count);
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(membershipPlanService.convertEntityToDTO(any(MembershipPlan.class))).thenReturn(planDTO);
        // WHEN
        MemberDTO response = memberService.saveNewMember(memberDTO, gymId, planId);
        // THEN
        assertNotNull(response);
        assertEquals(memberDTO.getFullname(), response.getFullname());
        assertEquals(memberDTO.getEmail(), response.getEmail());
        verify(membershipPlanRepository, times(1)).findById(planId);
        verify(memberRepository, times(1)).countByMembershipPlanIdAndStatus(planId, MemberStatus.ACTIVE);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    // test for exception EntityNotFoundException in method 'saveNewMember' when plan is not found
    @Test
    void saveNewMember_ShouldThrowEntityNotFoundException() {
        // GIVEN
        Long gymId = 1L;
        Long planId = 1L;
        MemberDTO memberDTO = createMemberDTO();
        when(membershipPlanRepository.findById(planId)).thenReturn(Optional.empty());
        // WHEN
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> memberService.saveNewMember(memberDTO, gymId, planId));
        // THEN
        assertEquals("Plan with ID: " + planId + " is not found.", exception.getMessage());
        verify(membershipPlanRepository, times(1)).findById(planId);
        verify(memberRepository, never()).save(any(Member.class));
    }

    // test for exception IllegalArgumentException in method 'saveNewMember' when plan does not belong to gym
    @Test
    void saveNewMember_ShouldThrowIllegalArgumentException() {
        // GIVEN
        Long gymId = 1L;
        Long planId = 1L;

        Gym gym = createGym();
        gym.setId(2L);

        MembershipPlan plan = createMembershipPlan(gym);

        MemberDTO memberDTO = createMemberDTO();

        when(membershipPlanRepository.findById(planId)).thenReturn(Optional.of(plan));
        // WHEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> memberService.saveNewMember(memberDTO, gymId, planId));
        // THEN
        assertEquals("Plan with ID: " + planId + " does not belong to Gym with ID: " + gymId, exception.getMessage());
        verify(membershipPlanRepository, times(1)).findById(planId);
        verify(memberRepository, never()).save(any(Member.class));
    }

    // test for exception IllegalArgumentException in method 'saveNewMember' when plan has max number of members
    @Test
    void saveNewMember_MaxMembers_ShouldThrowIllegalArgumentException() {
        // GIVEN
        Long gymId = 1L;
        Long planId = 1L;

        Long count = 3L;

        Gym gym = createGym();
        gym.setId(gymId);

        MembershipPlan plan = createMembershipPlan(gym);
        plan.setMaxMembers(3);

        MemberDTO memberDTO = createMemberDTO();

        when(membershipPlanRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(memberRepository.countByMembershipPlanIdAndStatus(planId, MemberStatus.ACTIVE)).thenReturn(count);
        // WHEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> memberService.saveNewMember(memberDTO, gymId, planId));
        // THEN
        assertEquals("Plan with ID: " + planId + " has max number of members", exception.getMessage());
        verify(membershipPlanRepository, times(1)).findById(planId);
        verify(memberRepository, times(1)).countByMembershipPlanIdAndStatus(planId, MemberStatus.ACTIVE);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void getAllMembers_ShouldReturnListOfMemberDTO() {
        // GIVEN
        Gym gym = createGym();

        MembershipPlan plan = createMembershipPlan(gym);
        plan.setId(1L);

        MembershipPlanDTO planDTO = createMembershipPlanDTO();
        planDTO.setId(1L);

        Member member1 = createMember();
        member1.setMembershipPlan(plan);

        Member member2 = createMember();
        member2.setId(2L);
        member2.setFullname("Siarhei Ushkevich");
        member2.setEmail("siarheiushkevich@gmail.com");
        member2.setStatus(MemberStatus.CANCELLED);
        member2.setMembershipPlan(plan);

        List<Member> memberList = List.of(member1, member2);
        when(memberRepository.findAll()).thenReturn(memberList);
        when(membershipPlanService.convertEntityToDTO(any(MembershipPlan.class))).thenReturn(planDTO);
        // WHEN
        List<MemberDTO> response = memberService.getAllMembers();
        // THEN
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(1L, response.get(0).getId());

        assertEquals("Valiantsin Ushkevich", response.get(0).getFullname());
        assertEquals("vushkevih@gmail.com", response.get(0).getEmail());
        assertEquals(MemberStatus.ACTIVE, response.get(0).getStatus());
        assertEquals("Lux", response.get(0).getMembershipPlanDTO().getName());

        assertEquals("Siarhei Ushkevich", response.get(1).getFullname());
        assertEquals("siarheiushkevich@gmail.com", response.get(1).getEmail());
        assertEquals(MemberStatus.CANCELLED, response.get(1).getStatus());
        assertEquals("Lux", response.get(1).getMembershipPlanDTO().getName());
        verify(memberRepository, times(1)).findAll();
    }

    // test for success method 'cancelMembership'
    @Test
    void cancelMembership_ShouldReturnMemberDTOWithStatusCancelled() {
        // GIVEN
        Long memberId = 1L;

        Gym gym = createGym();
        MembershipPlan plan = createMembershipPlan(gym);

        Member member = createMember();
        member.setMembershipPlan(plan);

        Member cancelledMember = createMember();
        cancelledMember.setMembershipPlan(plan);
        cancelledMember.setStatus(MemberStatus.CANCELLED);

        MembershipPlanDTO planDTO = createMembershipPlanDTO();
        planDTO.setId(1L);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(cancelledMember);
        when(membershipPlanService.convertEntityToDTO(any(MembershipPlan.class))).thenReturn(planDTO);
        // WHEN
        MemberDTO response = memberService.cancelMembership(memberId);
        // THEN
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Valiantsin Ushkevich", response.getFullname());
        assertEquals("vushkevih@gmail.com", response.getEmail());
        assertEquals(MemberStatus.CANCELLED, response.getStatus());
        verify(memberRepository, times(1)).findById(memberId);
        verify(membershipPlanService, times(1)).convertEntityToDTO(any(MembershipPlan.class));
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    // test for exception EntityNotFoundException in method 'cancelMembership' when member is not found.
    @Test
    void cancelMembership_ShouldThrowEntityNotFoundException() {
        // GIVEN
        Long memberId = 1L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
        // WHEN
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> memberService.cancelMembership(memberId));
        // THEN
        assertEquals("Member with ID: " + memberId + " is not found.", exception.getMessage());
        verify(memberRepository, times(1)).findById(memberId);
        verify(memberRepository, never()).save(any(Member.class));
    }

    // test for exception IllegalStateException in method 'cancelMembership' when member's status is already CANCELLED.
    @Test
    void cancelMembership_ShouldThrowIllegalStateException() {
        // GIVEN
        Long memberId = 1L;

        Member member = createMember();
        member.setStatus(MemberStatus.CANCELLED);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        // WHEN
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> memberService.cancelMembership(memberId));
        // THEN
        assertEquals("Member's status is already CANCELLED.", exception.getMessage());
        verify(memberRepository, times(1)).findById(memberId);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void convertDTOtoEntity_ShouldReturnMember() {
        // GIVEN
        MemberDTO memberDTO = createMemberDTO();
        // WHEN
        Member response = memberService.convertDTOtoEntity(memberDTO);
        // THEN
        assertNotNull(response);
        assertEquals(memberDTO.getFullname(), response.getFullname());
        assertEquals(memberDTO.getEmail(), response.getEmail());
    }

    @Test
    void convertEntityToDTO_ShouldReturnMemberDTO() {
        // GIVEN
        Gym gym = createGym();
        MembershipPlan plan = createMembershipPlan(gym);

        Member member = createMember();
        member.setMembershipPlan(plan);

        MembershipPlanDTO planDTO = createMembershipPlanDTO();

        when(membershipPlanService.convertEntityToDTO(any(MembershipPlan.class))).thenReturn(planDTO);
        // WHEN
        MemberDTO response = memberService.convertEntityToDTO(member);
        // THEN
        assertNotNull(response);
        assertEquals(member.getId(), response.getId());
        assertEquals(member.getFullname(), response.getFullname());
        assertEquals(member.getEmail(), response.getEmail());
        assertEquals(member.getStatus(), response.getStatus());
        assertEquals(member.getStartDate(), response.getStartDate());
    }
}
