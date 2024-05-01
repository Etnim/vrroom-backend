package com.vrrom.admin.service;

import com.vrrom.admin.Admin;
import com.vrrom.admin.exception.AdminNotFoundException;
import com.vrrom.admin.mapper.AdminMapper;
import com.vrrom.admin.dtos.AdminDTO;
import com.vrrom.admin.repository.AdminRepository;
import com.vrrom.application.exception.ApplicationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    public AdminService(AdminRepository adminRepository, AdminMapper adminMapper) {
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
    }

    public Admin findAdminById(long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new  AdminNotFoundException("Admin not found with ID: " + adminId));
    }

    public AdminDTO findByUid(String uid) {
        Admin admin = adminRepository.findByUid(uid);
        if (admin == null) {
            throw new AdminNotFoundException("Admin not found with UID: " + uid);
        }
        return adminMapper.toDTO(admin);
    }

    public AdminDTO findByEmail(String email) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            throw new AdminNotFoundException("Admin not found with email: " + email);
        }
        return adminMapper.toDTO(admin);
    }

    public boolean isSuperAdmin() {
        String uid = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        AdminDTO adminDTO = findByUid(uid);
        return adminDTO.getRole() == Admin.AdminRole.SUPER_ADMIN;
    }
}
