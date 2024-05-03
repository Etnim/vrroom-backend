package com.vrrom.admin.repository;

import com.vrrom.admin.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminRepository extends JpaRepository<Admin, Long>, JpaSpecificationExecutor<Admin> {
    Admin findByUid(String uid);
    Admin findByEmail(String email);
}
