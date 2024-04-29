package com.vrrom.application.service;

import com.vrrom.application.dto.ApplicationListDTO;
import com.vrrom.application.dto.ApplicationListDTOWithHistory;
import com.vrrom.application.dto.ApplicationRequest;
import com.vrrom.application.dto.ApplicationResponse;
import com.vrrom.application.exception.ApplicationException;
import com.vrrom.application.mapper.ApplicationListDTOMapper;
import com.vrrom.application.mapper.ApplicationMapper;
import com.vrrom.application.model.AgreementInfo;
import com.vrrom.application.model.Application;
import com.vrrom.application.model.ApplicationSortParameters;
import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.application.repository.ApplicationRepository;
import com.vrrom.application.util.ApplicationSpecifications;
import com.vrrom.applicationStatusHistory.dto.ApplicationStatusHistoryDTO;
import com.vrrom.applicationStatusHistory.mapper.ApplicationStatusHistoryMapper;
import com.vrrom.applicationStatusHistory.model.ApplicationStatusHistory;
import com.vrrom.applicationStatusHistory.service.ApplicationStatusHistoryService;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final EmailService emailService;
    private final AdminService adminService;
    private final PdfGenerator pdfGenerator;
    private final DownloadTokenService downloadTokenService;
    private final ApplicationStatusHistoryService applicationStatusHistoryService;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, EmailService emailService, AdminService adminService, PdfGenerator pdfGenerator, DownloadTokenService downloadTokenService, ApplicationStatusHistoryService applicationStatusHistoryService) {
        this.applicationRepository = applicationRepository;
        this.emailService = emailService;
        this.adminService = adminService;
        this.pdfGenerator = pdfGenerator;
        this.downloadTokenService = downloadTokenService;
        this.applicationStatusHistoryService = applicationStatusHistoryService;
    }

    @Transactional
    public ApplicationResponse createApplication(ApplicationRequest applicationRequest) {
        try {
            Application application = new Application();
            updateApplication(applicationRequest, application);
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

    public CustomPage<ApplicationListDTO> findPaginatedApplications(
            int pageNo, int pageSize,
            ApplicationSortParameters sortField,
            String sortDir,
            Long customerId,
            Long managerId,
            String managerFullName,
            ApplicationStatus status,
            LocalDate startDate,
            LocalDate endDate,
            boolean includeHistory) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortField.getValue());
            Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
            Specification<Application> spec = buildSpecification(customerId, managerId, managerFullName, status, startDate, endDate);
            Page<Application> page = applicationRepository.findAll(spec, pageable);
            CustomPage<ApplicationListDTO> result = toCustomPage(page);
            if (includeHistory) {
                List<ApplicationListDTO> enhancedDtos = new ArrayList<>();
                for (ApplicationListDTO dto : result.getContent()) {
                    if (dto instanceof ApplicationListDTOWithHistory detailedDto) {
                        List<ApplicationStatusHistory> history = applicationStatusHistoryService.getApplicationStatusHistory(detailedDto.getApplicationId());
                        List<ApplicationStatusHistoryDTO> historyDTOs = history.stream()
                                .map(ApplicationStatusHistoryMapper::toApplicationStatusHistoryDTO)
                                .collect(Collectors.toList());
                        detailedDto.setStatusHistory(historyDTOs);
                        enhancedDtos.add(detailedDto);
                    } else {
                        enhancedDtos.add(applicationStatusHistoryService.enhanceDtoWithHistory(dto));
                    }
                }
                result.setContent(enhancedDtos);
            }
            return result;
        } catch (IllegalArgumentException e) {
            throw new ApplicationException("Invalid request parameters", e);
        } catch (Exception e) {
            throw new ApplicationException("An error occurred while processing the applications", e);
        }
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

    private Specification<Application> buildSpecification(Long customerId, Long managerId, String managerFullName, ApplicationStatus status, LocalDate startDate, LocalDate endDate) {
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
    public ApplicationResponse findApplicationById(long id) {
        Optional<Application> application = applicationRepository.findById(id);
        return ApplicationMapper.toResponse(application.orElseThrow());
    }

    @Transactional
    public ApplicationResponse updateApplication(long id, ApplicationRequest applicationRequest) {
        try {
            Optional<Application> optionalApplication = applicationRepository.findById(id);
            if (optionalApplication.isEmpty()) {
                throw new ApplicationException("Application not found with id: " + id);
            }
            Application existingApplication = optionalApplication.get();
            updateApplication(applicationRequest, existingApplication);
            emailService.sendEmail("vrroom.leasing@gmail.com", existingApplication.getCustomer().getEmail(), "Application Update", "Your application has been updated successfully.");
            return ApplicationMapper.toResponse(existingApplication);
        } catch (DataAccessException dae) {
            throw new ApplicationException("Failed to save application data", dae);
        } catch (MailException me) {
            throw new ApplicationException("Failed to send notification email", me);
        } catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while updating the application", e);
        }
    }

    private void updateApplication(ApplicationRequest applicationRequest, Application application) {
        Customer customer = CustomerMapper.toEntity(applicationRequest.getCustomer(), application);
        FinancialInfo financialInfo = FinancialInfoMapper.toEntity(applicationRequest.getFinancialInfo(), application);
        VehicleDetails vehicleDetails = VehicleMapper.toEntity(applicationRequest.getVehicleDetails(), application);
        ApplicationMapper.toEntity(
                application,
                applicationRequest,
                customer,
                financialInfo,
                vehicleDetails);
        applicationRepository.save(application);
        if (application.getStatus() == ApplicationStatus.WAITING_FOR_SIGNING) {
            String baseUrl = "http://localhost:8080";
            String token = downloadTokenService.generateToken(application);
            String encodedAgreementUrl = UrlBuilder.createEncodedUrl(baseUrl, "agreement", token);
            emailService.sendEmail("vrroom.leasing@gmail.com", "vrroom.leasing@gmail.com", "Application Approved", "Your application has been approved successfully. Please click here to download your agreement: " + encodedAgreementUrl);
        }
    }
    public Application findApplicationById(long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationException("No such application found"));
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

    public void updateApplicationStatus(long id, ApplicationStatus status) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found", new Throwable("ID: " + id)));
        application.setStatus(status);
        applicationRepository.save(application);
        if (application.getStatus() == ApplicationStatus.WAITING_FOR_SIGNING) {
            String baseUrl = "http://localhost:8080";
            String token = downloadTokenService.generateToken(application);
            String encodedAgreementUrl = UrlBuilder.createEncodedUrl(baseUrl, "agreement", token);
            emailService.sendEmail("vrroom.leasing@gmail.com", "vrroom.leasing@gmail.com", "Application Approved", "Your application has been approved successfully. Please click here to download your agreement: " + encodedAgreementUrl);
        }
    }
}


