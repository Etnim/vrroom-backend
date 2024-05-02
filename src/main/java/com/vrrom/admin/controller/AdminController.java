package com.vrrom.admin.controller;

import com.vrrom.admin.dtos.AdminDTO;
import com.vrrom.admin.service.AdminService;
import com.vrrom.application.dto.ApplicationResponseAdminDetails;
import com.vrrom.application.mapper.ApplicationMapper;
import com.vrrom.application.model.Application;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping(value = "/admins")
@Tag(name = "Admin Controller", description = "To work with admins")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get admins")
    public List<AdminDTO> getApplicationById() {
        return adminService.findAll();
    }
}
