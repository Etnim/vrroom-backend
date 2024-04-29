package com.vrrom.admin.controller;

import com.vrrom.admin.Admin;
import com.vrrom.admin.dtos.AdminDTO;
import com.vrrom.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admins")
@Tag(name = "Admin Controller", description = "To work with admin data")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/admin")
    @Operation(summary = "Create admin")
    public Admin createAdmin(@RequestBody AdminDTO admin) {
        return adminService.createAdmin(admin);
    }

    @GetMapping(value = "/{id}")
    public Admin findAdminById(long id){
        return adminService.findAdminById(id);
    }
}
