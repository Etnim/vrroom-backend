package com.vrrom.application.controller;

import com.vrrom.application.model.ApplicationDTO;
import com.vrrom.application.model.ApplicationListDTO;
import com.vrrom.application.service.ApplicationService;
import com.vrrom.util.SanitizationUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;


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
    public Page<ApplicationListDTO> getPaginatedApplications(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "5") int size,
                                                             @RequestParam(defaultValue = "applicationCreatedDate") String sortField,
                                                             @RequestParam(defaultValue = "desc") String sortDir,
                                                             @RequestParam(required = false) Long managerId,
                                                             @RequestParam(required = false) String status,
                                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        int MAX_PAGE_SIZE = 20;
        if (page < 0 || size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("Invalid page or size parameters.");
        }
        if (!SanitizationUtils.isValidSortField(sortField, sortDir)) {
            throw new IllegalArgumentException(new Throwable("Invalid sorting parameters."));
        }
        if (startDate != null && endDate != null) {
            if (!startDate.isBefore(endDate)) {
                throw new IllegalArgumentException(new Throwable("Invalid date period."));
            }
        }
        if (status != null && !SanitizationUtils.isApplicationStatus(status)) {
            throw new IllegalArgumentException(new Throwable("Invalid application status."));
        }
        if (managerId != null && managerId < 0) {
            throw new IllegalArgumentException(new Throwable("Invalid manager id."));
        }
        return applicationService.findPaginatedApplications(page, size, sortField, sortDir, managerId, status, startDate, endDate);
    }
}
