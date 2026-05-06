package com.test_task.sii.repository;

import com.test_task.sii.entity.Gym;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymRepository extends JpaRepository<Gym, Long> {
    boolean existsByName(String name);
}
