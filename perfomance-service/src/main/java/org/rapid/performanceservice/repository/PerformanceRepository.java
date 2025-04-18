package org.rapid.performanceservice.repository;

import org.rapid.performanceservice.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance,Long> {
    @Query("SELECT p FROM Performance p WHERE p.repository=:repository ORDER BY p.timestamp DESC LIMIT 30")
    List<Performance> getPerformance(org.rapid.performanceservice.entity.Repository repository);
}
