package com.vrrom.admin.mapper;

import com.vrrom.admin.Admin;
import com.vrrom.admin.dtos.AdminDTO;

public class AdminMapper {

    public static Admin toEntity(AdminDTO adminDTO) {
        Admin admin = new Admin();
        admin.setName(adminDTO.getName());
        admin.setSurname(adminDTO.getSurname());
        return admin;
    }

    public static AdminDTO toDTO(Admin admin){
        return  admin != null
                ? new AdminDTO(admin.getId(), admin.getName(), admin.getSurname())
                : null;
    }
}
