package com.vrrom.application.controller;

import com.vrrom.application.model.Application;
import com.vrrom.application.model.ApplicationDTO;
import com.vrrom.application.service.ApplicationService;
import com.vrrom.customer.Customer;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/applications")
@Tag(name = "Application Controller", description = "To work with application data")
public class ApplicationController {
    private final ApplicationService applicationService;
    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }
    @PostMapping
    public ResponseEntity<String> createApplication(@RequestBody ApplicationDTO applicationDTO) {
         applicationService.createApplication(applicationDTO);
        return ResponseEntity.ok("The application was successfully saved");
    }
}
