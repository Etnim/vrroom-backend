package com.vrrom.admin.mapper;

import com.vrrom.admin.Admin;
import com.vrrom.admin.dtos.AdminDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AdminMapper {

    public static void toEntity(Admin admin, AdminDTO adminDTO) {
        admin.setName(adminDTO.getName());
        admin.setSurname(admin.getSurname());
        admin.setEmail(adminDTO.getEmail());
        admin.setRole(adminDTO.getRole());
        admin.setEmail(adminDTO.getEmail());
        admin.setUid(adminDTO.getUid());
        admin.setAssignedApplications(new ArrayList<>());
        admin.setComments(new ArrayList<>());
    }

    public static AdminDTO toDTO(Admin admin) {
        return admin != null
                ? new AdminDTO(admin.getUid(), admin.getEmail(), admin.getName(), admin.getSurname(), admin.getRole())
                : null;
    }
}