package com.vrrom.application.controller;

import com.vrrom.application.service.ApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
}
