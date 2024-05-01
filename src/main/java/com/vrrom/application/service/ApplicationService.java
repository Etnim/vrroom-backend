package com.vrrom.application.service;

import com.vrrom.admin.Admin;
import com.vrrom.admin.service.AdminService;
import com.vrrom.application.dto.ApplicationPage;
import com.vrrom.application.dto.ApplicationRequest;
import com.vrrom.application.dto.ApplicationRequestFromAdmin;
import com.vrrom.application.dto.ApplicationResponse;
import com.vrrom.application.dto.ApplicationResponseFromAdmin;
import com.vrrom.application.exception.ApplicationException;
import com.vrrom.application.exception.ApplicationNotFoundException;
import com.vrrom.application.mapper.AgreementMapper;
import com.vrrom.application.mapper.ApplicationMapper;
import com.vrrom.application.mapper.ApplicationPageMapper;
import com.vrrom.application.model.AgreementInfo;
import com.vrrom.application.model.Application;
import com.vrrom.application.model.ApplicationSortParameters;
import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.application.repository.ApplicationRepository;
import com.vrrom.application.util.ApplicationSpecifications;
import com.vrrom.application.util.CustomPageBase;
import com.vrrom.application.util.CustomPageWithHistory;
import com.vrrom.applicationStatusHistory.exception.ApplicationStatusHistoryException;
import com.vrrom.applicationStatusHistory.service.ApplicationStatusHistoryService;
import com.vrrom.customer.Customer;
import com.vrrom.customer.mappers.CustomerMapper;
import com.vrrom.customer.service.CustomerService;
import com.vrrom.dowloadToken.exception.DownloadTokenException;
import com.vrrom.dowloadToken.service.DownloadTokenService;
import com.vrrom.email.service.EmailService;
import com.vrrom.financialInfo.mapper.FinancialInfoMapper;
import com.vrrom.financialInfo.model.FinancialInfo;
import com.vrrom.util.PdfGenerator;
import com.vrrom.util.StatisticsService;
import com.vrrom.util.UrlBuilder;
import com.vrrom.util.exceptions.DatabaseException;
import com.vrrom.util.exceptions.EntityMappingException;
import com.vrrom.util.exceptions.PdfGenerationException;
import com.vrrom.util.exceptions.StatisticsException;
import com.vrrom.validation.ValidationService;
import com.vrrom.vehicle.mapper.VehicleMapper;
import com.vrrom.vehicle.model.VehicleDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final EmailService emailService;
    private final AdminService adminService;
    private final PdfGenerator pdfGenerator;
    private final CustomerService customerService;
    private final ApplicationStatusHistoryService applicationStatusHistoryService;
    private final StatisticsService statisticsService;
    private final DownloadTokenService downloadTokenService;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, EmailService emailService, AdminService adminService, CustomerService customerService, PdfGenerator pdfGenerator, ApplicationStatusHistoryService applicationStatusHistoryService, DownloadTokenService downloadTokenService, StatisticsService statisticsService) {
        this.applicationRepository = applicationRepository;
        this.emailService = emailService;
        this.adminService = adminService;
        this.pdfGenerator = pdfGenerator;
        this.customerService = customerService;
        this.applicationStatusHistoryService = applicationStatusHistoryService;
        this.statisticsService = statisticsService;
        this.downloadTokenService = downloadTokenService;
    }

    public Application findApplicationById(long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationException("No such application found"));
    }

    @Transactional
    public ApplicationResponse createApplication(ApplicationRequest applicationRequest) {
        try {
            Application application = new Application();
            populateNewApplicationWithRequest(applicationRequest, application);
            applicationRepository.save(application);
            applicationStatusHistoryService.addApplicationStatusHistory(application);
            emailService.sendEmail("vrroom.leasing@gmail.com", application.getCustomer().getEmail(), "Application", "Your application has been created successfully.");
            return ApplicationMapper.toResponse(application);
        } catch (DataAccessException dae) {
            throw new ApplicationException("Failed to save application data", dae);
        } catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while creating the application", e);
        }
    }

    @Transactional
    public ApplicationResponse updateApplication(long id, ApplicationRequest applicationRequest) {
        try {
            Application application = findApplicationById(id);
            populateExistingApplicationWithRequest(applicationRequest, application);
            applicationRepository.save(application);
            applicationStatusHistoryService.addApplicationStatusHistory(application);
            sendEmail(application, "Application Update", "Your application has been updated successfully.");
            return ApplicationMapper.toResponse(application);
        } catch (DataAccessException dae) {
            throw new ApplicationException("Failed to save application data", dae);
        } catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while updating the application", e);
        }
    }

    @Transactional
    public ApplicationResponseFromAdmin updateApplicationFromAdmin(long applicationId, ApplicationRequestFromAdmin applicationRequest, long adminId) {
        try {
            Application application = findApplicationById(applicationId);
            Admin admin = adminService.findAdminById(adminId);
            if (application.getManager() == null) {
                throw new ApplicationException("Admin is not assigned to this application");
            }
            if (application.getManager().getId() != adminId) {
                throw new ApplicationException("This admin is not assigned to this application");
            }
            ApplicationMapper.toEntityFromAdmin(application, applicationRequest, admin);
            applicationRepository.save(application);
            applicationStatusHistoryService.addApplicationStatusHistory(application);
            sendEmail(application, "Application Update By Admin", "Your application has been updated by admin.");
            return ApplicationMapper.toResponseFromAdmin(application);
        } catch (DataAccessException dae) {
            throw new ApplicationException("Failed to save application data", dae);
        } catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while updating the application", e);
        }
    }

    public CustomPageBase<ApplicationPage> findPaginatedApplications(
            int pageNo,
            int pageSize,
            ApplicationSortParameters sortField,
            String sortDir,
            Long customerId,
            Long managerId,
            String managerFullName,
            ApplicationStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            boolean includeHistory) throws StatisticsException {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortField.getValue());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Specification<Application> spec = buildSpecification(customerId, managerId, managerFullName, status, startDate, endDate);
        Page<Application> page = applicationRepository.findAll(spec, pageable);
        if (includeHistory) {
            CustomPageWithHistory<ApplicationPage> result = new CustomPageWithHistory<>();
            populateBasePageData(result, page);
            LocalDateTime startDateTime = startDate != null ? startDate : LocalDateTime.of(1970, 1, 1, 0, 0);
            LocalDateTime endDateTime = endDate != null ? endDate : LocalDateTime.now();
            result.setNumberOfApplications(statisticsService.countApplications(Optional.ofNullable(managerId), startDateTime, endDateTime));
            result.setAverageTimeToSignOrCancel(statisticsService.calculateAverageTimeFromSubmitToSignOrCancel(startDateTime, endDateTime, Optional.ofNullable(managerId)));
            result.setAverageTimeFromSubmitToAssigned(statisticsService.calculateAverageTimeFromSubmitToAssigned(Optional.ofNullable(managerId), startDateTime, endDateTime));
            return result;
        } else {
            CustomPageBase<ApplicationPage> result = new CustomPageBase<>();
            populateBasePageData(result, page);
            return result;
        }
    }

    private void populateBasePageData(CustomPageBase<ApplicationPage> page, Page<Application> data) {
        page.setContent(data.getContent().stream().map(ApplicationPageMapper::toApplicationListDTO).collect(Collectors.toList()));
        page.setPageNumber(data.getNumber());
        page.setPageSize(data.getSize());
        page.setTotalElements(data.getTotalElements());
        page.setTotalPages(data.getTotalPages());
        page.setSort(data.getSort().stream().map(order -> {
            ApplicationSortParameters param = ApplicationSortParameters.fromDisplayName(order.getProperty());
            return (param != null ? param.getRequestValue() : order.getProperty()) + "," + order.getDirection();
        }).collect(Collectors.toList()));
    }

    private Specification<Application> buildSpecification(Long customerId, Long managerId, String managerFullName, ApplicationStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        Specification<Application> spec = Specification.where(null);
        if (customerId != null) {
            spec = spec.and(ApplicationSpecifications.hasCustomer(customerId));
        }
        if (managerId != null) {
            spec = spec.and(ApplicationSpecifications.hasManager(managerId));
        }
        if (managerFullName != null) {
            spec = spec.and(ApplicationSpecifications.hasManagerFullName(managerFullName));
        }
        if (status != null) {
            spec = spec.and(ApplicationSpecifications.hasStatus(status));
        }
        if (startDate != null && endDate != null) {
            if (ValidationService.isValidDateRange(startDate, endDate)) {
                spec = spec.and(ApplicationSpecifications.isCreatedBetween(startDate, endDate));
            } else {
                throw new IllegalArgumentException(new Throwable("Invalid date range"));
            }
        } else {
            if (startDate != null) {
                spec = spec.and(ApplicationSpecifications.isCreatedAfter(startDate));
            }
            if (endDate != null) {
                spec = spec.and(ApplicationSpecifications.isCreatedBefore(endDate));
            }
        }
        return spec;
    }

    @Transactional
    public String assignAdmin(long adminId, long applicationId) throws ApplicationStatusHistoryException {
        Application application = findApplicationById(applicationId);
        if (application.getManager() != null) {
            throw new ApplicationException("Application is already assigned to a manager");
        }
        Admin admin = adminService.findAdminById(adminId);
        application.setManager(admin);
        application.setStatus(ApplicationStatus.UNDER_REVIEW);
        applicationStatusHistoryService.addApplicationStatusHistory(application);
        admin.getAssignedApplications().add(application);
        return "Admin " + application.getManager().getName()+ " is successfully assigned to: " + application.getId();
    }

    private void populateNewApplicationWithRequest(ApplicationRequest applicationRequest, Application application) {
        Customer customer = resolveCustomer(applicationRequest, application);
        VehicleDetails vehicleDetails = new VehicleDetails();
        FinancialInfo financialInfo = new FinancialInfo();
        populateCommonApplicationDetails(applicationRequest, application, customer, vehicleDetails, financialInfo);
    }

    private void populateExistingApplicationWithRequest(ApplicationRequest applicationRequest, Application application) {
        Customer customer = CustomerMapper.toEntity(applicationRequest.getCustomer(), application);
        FinancialInfo financialInfo = application.getFinancialInfo();
        VehicleDetails vehicleDetails = application.getVehicleDetails();
        populateCommonApplicationDetails(applicationRequest, application, customer, vehicleDetails, financialInfo);
    }

    private Customer resolveCustomer(ApplicationRequest applicationRequest, Application application) {
        Customer customer = customerService.findCustomerById(applicationRequest.getCustomer().getPersonalId());
        if (customer == null) {
            customer = CustomerMapper.toEntity(applicationRequest.getCustomer(), application);
        }
        return customer;
    }

    private void sendEmail(Application application, String subject, String message) {
        try {
            emailService.sendEmail("vrroom.leasing@gmail.com", application.getCustomer().getEmail(), subject, message);
        } catch (MailException me) {
            throw new ApplicationException("Failed to send notification email", me);
        }
    }

    private void populateCommonApplicationDetails(ApplicationRequest applicationRequest, Application application, Customer customer, VehicleDetails vehicleDetails, FinancialInfo financialInfo) {
        FinancialInfoMapper.toEntity(financialInfo, applicationRequest.getFinancialInfo(), application);
        VehicleMapper.toEntity(vehicleDetails, applicationRequest.getVehicleDetails(), application);
        ApplicationMapper.toEntity(application, applicationRequest, customer, financialInfo, vehicleDetails);
    }

    public byte[] getLeasingAgreement(String token) throws PdfGenerationException, EntityMappingException, DatabaseException, ApplicationException, DownloadTokenException {
        Application application;
        AgreementInfo agreementInfo;
        try {
            long applicationId = downloadTokenService.getApplicationId(token);
            application = applicationRepository.findById(applicationId).orElseThrow(() -> new ApplicationNotFoundException("No such application found ", new Throwable("ID: " + applicationId)));
            agreementInfo = AgreementMapper.mapToAgreementInfo(application);
        } catch (DataAccessException e) {
            throw new DatabaseException("Database access error while retrieving application", e.getCause());
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new EntityMappingException("Error mapping application to agreement info", e.getCause());
        }
        return pdfGenerator.generateAgreement(agreementInfo);
    }

    public void updateApplicationStatus(long id, ApplicationStatus status) throws ApplicationStatusHistoryException {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found", new Throwable("ID: " + id)));
        application.setStatus(status);
        applicationRepository.save(application);
        applicationStatusHistoryService.addApplicationStatusHistory(application);
        if (application.getStatus() == ApplicationStatus.WAITING_FOR_SIGNING) {
            String baseUrl = "http://localhost:8080";
            String token = downloadTokenService.generateToken(application);
            String encodedAgreementUrl = UrlBuilder.createEncodedUrl(baseUrl, "applications", token, "agreement");
            emailService.sendEmail("vrroom.leasing@gmail.com", "vrroom.leasing@gmail.com", "Application Approved", "Your application has been approved successfully. Please click here to download your agreement: " + encodedAgreementUrl);
        }
    }
}


