package com.test_task.sii.repository;

import com.test_task.sii.entity.Gym;
import com.test_task.sii.entity.MembershipPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {
    boolean existsByNameAndGym_Id(String planName, Long gymId);
    List<MembershipPlan> findByGymId(Long gymId);
}
