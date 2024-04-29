package com.vrrom.application.repository;

import com.vrrom.application.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ApplicationRepository extends JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {
    @Query("SELECT COUNT(DISTINCT ash.id) FROM Application ash WHERE (:managerId IS NULL OR ash.manager.id = :managerId) AND ash.createdAt BETWEEN :from AND :to")
    long countByManagerIdAndDateRange(@Param("managerId") Long managerId, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
