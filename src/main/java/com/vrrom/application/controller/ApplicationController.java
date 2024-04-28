package com.vrrom.application.controller;

import com.vrrom.application.dto.ApplicationRequest;
import com.vrrom.application.dto.ApplicationResponse;
import com.vrrom.application.dto.ApplicationListDTO;
import com.vrrom.application.model.ApplicationSortParameters;
import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.application.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import com.vrrom.util.CustomPage;
import com.vrrom.validation.annotations.PositiveLong;
import com.vrrom.validation.annotations.ValidPageSize;
import com.vrrom.validation.annotations.ValidSortDirection;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.xml.bind.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Validated
@RequestMapping(value = "/applications")
@Tag(name = "Application Controller", description = "To work with application data")
public class ApplicationController {
    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CustomPage<ApplicationListDTO> getPaginatedApplications(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "5") @ValidPageSize Integer size,
                                                                   @RequestParam(defaultValue = "applicationCreatedDate") ApplicationSortParameters sortField,
                                                                   @RequestParam(defaultValue = "desc") @ValidSortDirection String sortDir,
                                                                   @RequestParam(required = false) @PositiveLong Long managerId,
                                                                   @RequestParam(required = false) ApplicationStatus status,
                                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                                   @RequestParam(defaultValue = "false") boolean isSuperAdmin) {
        return applicationService.findPaginatedApplications(page, size, sortField, sortDir, managerId, status, startDate, endDate, isSuperAdmin);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/application")
    @Operation(summary = "Create application")
    public ApplicationResponse createApplication(@RequestBody ApplicationRequest applicationRequest) {
        return applicationService.createApplication(applicationRequest);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse getApplicationById(@PathVariable long id) {
        return applicationService.findApplicationById(id);
    }

    @PutMapping(value = "/{id}")
    public ApplicationResponse updateApplication(@PathVariable long id, @RequestBody ApplicationRequest applicationRequest) {
        return applicationService.updateApplication(id, applicationRequest);
    }
}
