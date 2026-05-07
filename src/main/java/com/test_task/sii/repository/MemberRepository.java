package com.test_task.sii.repository;

import com.test_task.sii.entity.Member;
import com.test_task.sii.entity.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Long countByMembershipPlanIdAndStatus(Long planId, MemberStatus status);
}
