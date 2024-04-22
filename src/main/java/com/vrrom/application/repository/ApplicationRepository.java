package com.vrrom.application.repository;

import com.vrrom.application.model.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationRepository extends JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {
    @Query("SELECT a FROM Application a WHERE a.manager.id = :adminId")
    Page<Application> findByAdminId(long adminId, Pageable pageable);
}
