package com.vrrom.application.controller;

import com.vrrom.agreement.exception.AgreementException;
import com.vrrom.agreement.service.AgreementService;
import com.vrrom.application.dto.ApplicationCustomerResponse;
import com.vrrom.application.dto.ApplicationPage;
import com.vrrom.application.dto.ApplicationRequest;
import com.vrrom.application.dto.ApplicationRequestFromAdmin;
import com.vrrom.application.dto.ApplicationResponseAdminDetails;
import com.vrrom.application.dto.ApplicationResponseFromAdmin;
import com.vrrom.application.mapper.ApplicationMapper;
import com.vrrom.application.model.Application;
import com.vrrom.application.model.ApplicationSortParameters;
import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.application.service.ApplicationService;
import com.vrrom.application.util.CustomPageBase;
import com.vrrom.applicationStatusHistory.exception.ApplicationStatusHistoryException;
import com.vrrom.dowloadToken.exception.DownloadTokenException;
import com.vrrom.email.exception.EmailServiceException;
import com.vrrom.util.exceptions.DatabaseException;
import com.vrrom.util.exceptions.EntityMappingException;
import com.vrrom.util.exceptions.PdfGenerationException;
import com.vrrom.util.exceptions.StatisticsException;
import com.vrrom.validation.annotations.PositiveLong;
import com.vrrom.validation.annotations.ValidPageSize;
import com.vrrom.validation.annotations.ValidSortDirection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@Validated
@RequestMapping(value = "/applications")
@Tag(name = "Application Controller", description = "To work with application data")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final AgreementService agreementService;

    @Autowired
    public ApplicationController(ApplicationService applicationService, AgreementService agreementService) {
        this.applicationService = applicationService;
        this.agreementService = agreementService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CustomPageBase<ApplicationPage> getPaginatedApplications(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "5") @ValidPageSize Integer size,
                                                                    @RequestParam(defaultValue = "applicationCreatedDate") ApplicationSortParameters sortField,
                                                                    @RequestParam(defaultValue = "desc") @ValidSortDirection String sortDir,
                                                                    @RequestParam(required = false) @PositiveLong Long customerId,
                                                                    @RequestParam(required = false) @PositiveLong Long managerId,
                                                                    @RequestParam(required = false) String managerFullName,
                                                                    @RequestParam(required = false) ApplicationStatus status,
                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                                                    @RequestParam(defaultValue = "false") boolean isSuperAdmin) throws StatisticsException {
        return applicationService.findPaginatedApplications(page, size, sortField, sortDir, customerId, managerId, managerFullName, status, startDate, endDate, isSuperAdmin);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value="/application")
    @Operation(summary = "Create application")
    public ApplicationResponseAdminDetails createApplication(@Valid @RequestBody ApplicationRequest applicationRequest) {
        return applicationService.createApplication(applicationRequest);
    }

    @GetMapping(value = "/{id}/customer")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get application")
    public ApplicationResponseAdminDetails getApplicationById(@PathVariable long id) {
        Application application = applicationService.findApplicationById(id);
        return ApplicationMapper.toAdminDetailsResponse(application);
    }
    @GetMapping(value="/customer/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get the application for user")
    public ApplicationCustomerResponse getApplicationByIdForCustomer(@PathVariable long id) {
        Application application = applicationService.findApplicationById(id);
        return ApplicationMapper.toCustomerResponse(application);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update application")
    public ApplicationResponseAdminDetails updateApplication(@PathVariable long id, @Valid @RequestBody ApplicationRequest applicationRequest) {
        return applicationService.updateApplication(id, applicationRequest);
    }

    @PutMapping(value = "/{applicationId}/admin")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update application from admin request")
    public ApplicationResponseFromAdmin updateApplicationFromAdmin(@PathVariable long applicationId, @Valid @RequestBody ApplicationRequestFromAdmin applicationRequest) {
        return applicationService.updateApplicationFromAdmin(applicationId,applicationRequest);
    }

    @PutMapping("/{applicationId}/assignAdmin")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Assign admin to application and change status of the application.")
    public String assignAdmin(@PathVariable long applicationId) throws ApplicationStatusHistoryException {
        return applicationService.assignAdmin(applicationId);
    }

    @PutMapping("/{applicationId}/assignAdmin/{newAdminUid}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Assign new admin to the application")
    public String reAssignAdmin(@PathVariable String newAdminUid, @PathVariable long applicationId) {
        return applicationService.reAssignAdmin(newAdminUid, applicationId);
    }

    @PutMapping("/{id}/updateStatus")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> updateApplicationStatus(@PathVariable long id, @RequestParam ApplicationStatus status) throws ApplicationStatusHistoryException, EmailServiceException {
        applicationService.updateApplicationStatus(id, status);
        return ResponseEntity.ok("Status updated successfully");
    }

    @GetMapping("/{token}/agreement")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getLeasingAgreement(@PathVariable String token) throws PdfGenerationException, EntityMappingException, DatabaseException, DownloadTokenException {
        byte[] pdfBytes = applicationService.getLeasingAgreement(token);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=agreement.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @PostMapping(value = "/{id}/agreement")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Upload signed agreement")
    public ResponseEntity<String> saveSignedAgreement(@RequestParam("file") MultipartFile signedAgreement, @PathVariable Long id) throws ApplicationStatusHistoryException, AgreementException {
        applicationService.saveSignedAgreement(id, signedAgreement);
        return ResponseEntity.ok("Status updated successfully");
    }

    @GetMapping("/{id}/signed-agreement")
    public ResponseEntity<byte[]> getAgreementContentById(@PathVariable Long id) {
        return agreementService.getAgreementById(id)
                .map(agreement -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=agreement.pdf");
                    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(agreement.getAgreementContent());
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}