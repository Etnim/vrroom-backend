package com.vrrom.admin.service;

import com.vrrom.admin.Admin;
import com.vrrom.admin.AdminMapper;
import com.vrrom.admin.AdminRequest;
import com.vrrom.admin.repository.AdminRepository;
import com.vrrom.application.dto.ApplicationResponse;
import com.vrrom.application.exception.ApplicationException;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin createAdmin(AdminRequest admin) {
        return adminRepository.save(AdminMapper.toEntity(admin));
    }
    public Admin findAdminById(long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new ApplicationException("No such admin found"));
    }
}
