package com.analytics.platform.repository;

import com.analytics.platform.entity.AutomationRun;
import com.analytics.platform.entity.AutomationRun.AutomationStatus;
import com.analytics.platform.entity.AutomationRun.AutomationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AutomationRunRepository extends JpaRepository<AutomationRun, String> {
    
    Page<AutomationRun> findByAutomationType(AutomationType automationType, Pageable pageable);
    Page<AutomationRun> findByStatus(AutomationStatus status, Pageable pageable);
    
    @Query("SELECT a FROM AutomationRun a WHERE " +
           "(:automationType IS NULL OR a.automationType = :automationType) AND " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:startDate IS NULL OR a.startTime >= :startDate) AND " +
           "(:endDate IS NULL OR a.startTime <= :endDate)")
    Page<AutomationRun> filterAutomationRuns(
        @Param("automationType") AutomationType automationType,
        @Param("status") AutomationStatus status,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );
    
    @Query("SELECT COUNT(a) FROM AutomationRun a WHERE a.status = :status")
    long countByStatus(@Param("status") AutomationStatus status);
}
