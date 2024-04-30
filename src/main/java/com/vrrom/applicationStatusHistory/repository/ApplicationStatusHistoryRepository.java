package com.vrrom.applicationStatusHistory.repository;

import com.vrrom.admin.Admin;
import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.applicationStatusHistory.model.ApplicationStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationStatusHistoryRepository extends JpaRepository<ApplicationStatusHistory, Long> {
    List<ApplicationStatusHistory> findByApplicationId(Long applicationId);
    ApplicationStatusHistory findByApplicationIdAndStatus(Long applicationId, ApplicationStatus status);

    @Query("SELECT ash FROM ApplicationStatusHistory ash WHERE ash.application.id = :applicationId AND ash.status = :status AND ash.changedAt > :date ORDER BY ash.changedAt ASC")
    Optional<ApplicationStatusHistory> findFirstByApplicationIdAndStatusAndDateAfter(Long applicationId, ApplicationStatus status, LocalDateTime date);

    long countByManagerAndChangedAtBetween(Admin admin, LocalDateTime start, LocalDateTime end);

    @Query("SELECT ash FROM ApplicationStatusHistory ash WHERE ash.status = :status AND ash.changedAt BETWEEN :start AND :end AND (:managerId IS NULL OR ash.manager.id = :managerId)")
    List<ApplicationStatusHistory> findByStatusChangedAtBetweenAndManagerId(ApplicationStatus status, LocalDateTime start, LocalDateTime end, Long managerId);

    @Query("SELECT ash FROM ApplicationStatusHistory ash WHERE ash.application.id = :applicationId AND ash.status = :status AND ash.changedAt > :changedAt AND (:managerId IS NULL OR ash.manager.id = :managerId) ORDER BY ash.changedAt ASC")
    Optional<ApplicationStatusHistory> findFirstByApplicationIdAndStatusAndDateAfterAndManagerId(Long applicationId, ApplicationStatus status, LocalDateTime changedAt, Long managerId);

    @Query(value = "SELECT AVG(EXTRACT(EPOCH FROM (assigned.changed_at - submitted.changed_at))) " +
            "FROM application_status_history AS submitted " +
            "INNER JOIN application_status_history AS assigned ON submitted.application_id = assigned.application_id " +
            "WHERE submitted.status = 'SUBMITTED' AND assigned.status = 'UNDER_REVIEW' " +
            "AND assigned.changed_at > submitted.changed_at " +
            "AND (:managerId IS NULL OR assigned.changed_by_manager_id = :managerId) " +
            "AND submitted.changed_at BETWEEN :from AND :to", nativeQuery = true)
    Double findAverageTimeFromSubmittedToAssignedByManagerAndDateRange(@Param("managerId") Long managerId, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}

