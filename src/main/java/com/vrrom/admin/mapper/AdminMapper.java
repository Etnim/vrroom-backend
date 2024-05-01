package com.vrrom.admin.mapper;

import com.vrrom.admin.Admin;
import com.vrrom.admin.dtos.AdminDTO;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

    public static Admin toEntity(AdminDTO adminDTO) {
        Admin admin = new Admin();
        admin.setName(adminDTO.getName());
        admin.setEmail(adminDTO.getEmail());
        admin.setRole(adminDTO.getRole());
        admin.setEmail(adminDTO.getEmail());
        admin.setUid(admin.getUid());
        return admin;
    }

    public static AdminDTO toDTO(Admin admin) {


        return admin != null
                ? new AdminDTO(admin.getUid(), admin.getEmail(), admin.getName(), admin.getSurname(), admin.getRole())
                : null;
    }
}