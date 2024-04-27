package com.vrrom.application.service;

import com.vrrom.admin.Admin;
import com.vrrom.admin.service.AdminService;
import com.vrrom.application.dto.ApplicationListDTO;
import com.vrrom.application.dto.ApplicationRequest;
import com.vrrom.application.dto.ApplicationResponse;
import com.vrrom.application.exception.ApplicationException;
import com.vrrom.application.exception.ApplicationNotFoundException;
import com.vrrom.util.UrlBuilder;
import com.vrrom.util.exceptions.DatabaseException;
import com.vrrom.util.exceptions.EntityMappingException;
import com.vrrom.util.exceptions.PdfGenerationException;
import com.vrrom.application.mapper.AgreementMapper;
import com.vrrom.application.mapper.ApplicationListDTOMapper;
import com.vrrom.application.mapper.ApplicationMapper;
import com.vrrom.application.model.AgreementInfo;
import com.vrrom.application.model.Application;
import com.vrrom.application.model.ApplicationSortParameters;
import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.application.repository.ApplicationRepository;
import com.vrrom.application.util.ApplicationSpecifications;
import com.vrrom.customer.Customer;
import com.vrrom.customer.mappers.CustomerMapper;
import com.vrrom.email.service.EmailService;
import com.vrrom.financialInfo.mapper.FinancialInfoMapper;
import com.vrrom.financialInfo.model.FinancialInfo;
import com.vrrom.util.CustomPage;
import com.vrrom.util.PdfGenerator;
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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final EmailService emailService;
    private final AdminService adminService;
    private final PdfGenerator pdfGenerator;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, EmailService emailService, AdminService adminService, PdfGenerator pdfGenerator) {
        this.applicationRepository = applicationRepository;
        this.emailService = emailService;
        this.adminService = adminService;
        this.pdfGenerator = pdfGenerator;
    }

    @Transactional
    public ApplicationResponse createApplication(ApplicationRequest applicationRequest) {
        try {
            Application application = new Application();
            populateApplicationWithRequest(applicationRequest, application);
            applicationRepository.save(application);
            emailService.sendEmail("vrroom.leasing@gmail.com", application.getCustomer().getEmail(), "Application", "Your application has been created successfully.");
            return ApplicationMapper.toResponse(application);
        } catch (DataAccessException dae) {
            throw new ApplicationException("Failed to save application data", dae);
        } catch (MailException me) {
            throw new ApplicationException("Failed to send notification email", me);
        } catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while creating the application", e);
        }
    }

    public CustomPage<ApplicationListDTO> findPaginatedApplications(int pageNo, int pageSize, ApplicationSortParameters sortField, String sortDir, Long managerId, ApplicationStatus status, LocalDate startDate, LocalDate endDate) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortField.getValue());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Specification<Application> spec = buildSpecification(managerId, status, startDate, endDate);
        Page<Application> page = applicationRepository.findAll(spec, pageable);
        return toCustomPage(page);
    }

    private CustomPage<ApplicationListDTO> toCustomPage(Page<Application> page) {
        List<ApplicationListDTO> content = page.getContent().stream()
                .map(ApplicationListDTOMapper::toApplicationListDTO)
                .collect(Collectors.toList());
        List<String> sortInfo = page.getSort().stream()
                .map(order -> {
                    ApplicationSortParameters param = ApplicationSortParameters.fromDisplayName(order.getProperty());
                    String jsonValue = param != null ? param.getRequestValue() : order.getProperty();
                    return jsonValue + "," + order.getDirection();
                })
                .collect(Collectors.toList());
        return new CustomPage<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                sortInfo
        );
    }

    private Specification<Application> buildSpecification(Long managerId, ApplicationStatus status, LocalDate startDate, LocalDate endDate) {
        Specification<Application> spec = Specification.where(null);
        if (managerId != null) {
            spec = spec.and(ApplicationSpecifications.hasManager(managerId));
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
    public ApplicationResponse updateApplication(long id, ApplicationRequest applicationRequest) {
        try {
            Application application = findApplicationById(id);
            populateApplicationWithRequest(applicationRequest, application);
            applicationRepository.save(application);
            emailService.sendEmail("vrroom.leasing@gmail.com", application.getCustomer().getEmail(), "Application Update", "Your application has been updated successfully.");
            return ApplicationMapper.toResponse(application);
        } catch (DataAccessException dae) {
            throw new ApplicationException("Failed to save application data", dae);
        } catch (MailException me) {
            throw new ApplicationException("Failed to send notification email", me);
        } catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while updating the application", e);
        }
    }

    @Transactional
    public String assignAdmin(long adminId, long applicationId) {
        Application application = findApplicationById(applicationId);
        if (application.getManager() != null) {
            throw new ApplicationException("Application is already assigned to a manager");
        }
        Admin admin = adminService.findAdminById(adminId);
        application.setManager(admin);
        application.setStatus(ApplicationStatus.UNDER_REVIEW);
        admin.getAssignedApplications().add(application);
        return "Admin " + application.getManager() + " is successfully assigned to: " + application.getId();
    }

    @Transactional
    public String removeAdmin(long applicationId) {
        Application application = findApplicationById(applicationId);
        if (application.getManager() == null) {
            throw new ApplicationException("Application is not assigned to any of managers");
        }
        application.setManager(null);
        return "Admin is successfully removed";
    }

    public String modifyInterestRate(long applicationId, double interestRate) {
        Application application = findApplicationById(applicationId);
        application.setInterestRate(interestRate);
        return "InterestRate is successfully update to: " + application.getInterestRate();
    }

    public String modifyAgreementFee(long applicationId, double interestRate) {
        Application application = findApplicationById(applicationId);
        application.setInterestRate(interestRate);
        return "InterestRate is successfully update to: " + application.getInterestRate();
    }

    private void populateApplicationWithRequest(ApplicationRequest applicationRequest, Application application) {
        Customer customer = CustomerMapper.toEntity(applicationRequest.getCustomer(), application);
        FinancialInfo financialInfo = FinancialInfoMapper.toEntity(applicationRequest.getFinancialInfo(), application);
        VehicleDetails vehicleDetails = VehicleMapper.toEntity(applicationRequest.getVehicleDetails(), application);
        ApplicationMapper.toEntity(
                application,
                applicationRequest,
                customer,
                financialInfo,
                vehicleDetails);
    }

    public Application findApplicationById(long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationException("No such application found"));
    }

    public byte[] getLeasingAgreement(Long applicationId) throws PdfGenerationException, EntityMappingException, DatabaseException, ApplicationException {
        Application application;
        AgreementInfo agreementInfo;
        try {
            application = applicationRepository.findById(applicationId).orElseThrow(() -> new ApplicationNotFoundException("No such application found ", new Throwable("ID: " + applicationId)));
            agreementInfo = AgreementMapper.mapToAgreementInfo(application);
        } catch (DataAccessException e) {
            throw new DatabaseException("Database access error while retrieving application", e.getCause());
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new EntityMappingException("Error mapping application to agreement info", e.getCause());
        }
        return pdfGenerator.generateAgreement(agreementInfo);
    }

    public void updateApplicationStatus(long id, ApplicationStatus status) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found", new Throwable("ID: " + id)));
        application.setStatus(status);
        applicationRepository.save(application);
        if (application.getStatus() == ApplicationStatus.WAITING_FOR_SIGNING) {
            String baseUrl = "http://localhost:8080";
            String encodedAgreementUrl = UrlBuilder.createEncodedUrl(baseUrl, "applications", String.valueOf(application.getId()), "agreement");
            emailService.sendEmail("vrroom.leasing@gmail.com", "vrroom.leasing@gmail.com", "Application Approved", "Your application has been approved successfully. Please click here to download your agreement: " + encodedAgreementUrl);
        }
    }
}


