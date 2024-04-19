package com.vrrom.application.controller;

import com.vrrom.application.model.ApplicationDTO;
import com.vrrom.application.model.ApplicationListDTO;
import com.vrrom.application.service.ApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ApplicationListDTO> getPaginatedApplications(@RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "5") int size,
                                                         @RequestParam(defaultValue = "createdAt") String sortField,
                                                         @RequestParam(defaultValue = "desc") String sortDir) {
        return applicationService.findPaginatedApplications(page, size, sortField, sortDir);
    }
}
