package com.test_task.sii.repository;

import com.test_task.sii.dto.ReportDTO;
import com.test_task.sii.entity.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface GymRepository extends JpaRepository<Gym, Long> {
    boolean existsByName(String name);

    // method gets list of reports regarding member's status = ACTIVE
    @Query("SELECT new com.test_task.sii.dto.ReportDTO(g.name, SUM(mp.monthlyPrice), mp.currency) " +
            "FROM Gym g " +
            "JOIN g.plans mp " +
            "JOIN mp.members m " +
            "WHERE m.status = ACTIVE " +
            "GROUP BY mp.currency, g.name")
    List<ReportDTO> getTotalReport();
}
