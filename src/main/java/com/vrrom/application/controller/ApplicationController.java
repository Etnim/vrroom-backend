package com.vrrom.application.controller;

import com.vrrom.application.dto.ApplicationDTO;
import com.vrrom.application.dto.ApplicationListDTO;
import com.vrrom.application.model.ApplicationSortParameters;
import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.application.service.ApplicationService;
import com.vrrom.application.dto.ApplicationSearchParams;
import com.vrrom.util.annotation.PositiveLong;
import com.vrrom.util.annotation.ValidDateRange;
import com.vrrom.util.annotation.ValidPageSize;
import com.vrrom.util.annotation.ValidSortDirection;
import com.vrrom.util.validator.DateRangeDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.xml.bind.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
                                                             @RequestParam(defaultValue = "5") @ValidPageSize Integer size,
                                                             @RequestParam(defaultValue = "applicationCreatedDate") ApplicationSortParameters sortField,
                                                             @RequestParam(defaultValue = "desc") @ValidSortDirection String sortDir,
                                                             @RequestParam(required = false) @PositiveLong Long managerId,
                                                             @RequestParam(required = false) ApplicationStatus status,
                                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws ValidationException {
        return applicationService.findPaginatedApplications(page, size, sortField, sortDir, managerId, status, startDate, endDate);
    }
}
