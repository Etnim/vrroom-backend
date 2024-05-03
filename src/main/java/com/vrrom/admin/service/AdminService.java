package com.vrrom.admin.service;

import com.vrrom.admin.Admin;
import com.vrrom.admin.exception.AdminNotFoundException;
import com.vrrom.admin.mapper.AdminMapper;
import com.vrrom.admin.dtos.AdminDTO;
import com.vrrom.admin.repository.AdminRepository;
import com.vrrom.application.exception.ApplicationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin findAdminById(long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new  AdminNotFoundException("Admin not found with ID: " + adminId));
    }

    public Admin findByUid(String uid) {
        return adminRepository.findByUid(uid) == null ? null : adminRepository.findByUid(uid) ;
    }

    public List<AdminDTO> findAll() {
       List<Admin> admins = adminRepository.findAll();
       List<AdminDTO> adminDTOs = new ArrayList<>();
       for(Admin a : admins){
           adminDTOs.add(AdminMapper.toDTO(a));
       }
       return adminDTOs;
    }
}
