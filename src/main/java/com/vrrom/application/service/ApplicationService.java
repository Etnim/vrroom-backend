package com.vrrom.application.service;

import com.twilio.exception.ApiException;
import com.vrrom.admin.model.Admin;
import com.vrrom.admin.service.AdminService;
import com.vrrom.agreement.exception.AgreementException;
import com.vrrom.agreement.service.AgreementService;
import com.vrrom.application.dto.ApplicationPage;
import com.vrrom.application.dto.ApplicationRequest;
import com.vrrom.application.dto.ApplicationRequestFromAdmin;
import com.vrrom.application.dto.ApplicationResponseAdminDetails;
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
import com.vrrom.comment.Comment;
import com.vrrom.customer.mappers.CustomerMapper;
import com.vrrom.customer.model.Customer;
import com.vrrom.customer.service.CustomerService;
import com.vrrom.dowloadToken.exception.DownloadTokenException;
import com.vrrom.dowloadToken.service.DownloadTokenService;
import com.vrrom.email.exception.EmailServiceException;
import com.vrrom.email.service.EmailService;
import com.vrrom.euribor.service.EuriborService;
import com.vrrom.financialInfo.mapper.FinancialInfoMapper;
import com.vrrom.financialInfo.model.FinancialInfo;
import com.vrrom.messageSender.MessageSender;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final AgreementService agreementService;
    private final EuriborService euriborService;
    private final MessageSender messageSender;
    private final EmailService emailNotificationService;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, EmailService emailService, AdminService adminService, CustomerService customerService, PdfGenerator pdfGenerator, ApplicationStatusHistoryService applicationStatusHistoryService, StatisticsService statisticsService, DownloadTokenService downloadTokenService, AgreementService agreementService, EuriborService euriborService, MessageSender messageSender, EmailService emailNotificationService) {
        this.applicationRepository = applicationRepository;
        this.emailService = emailService;
        this.adminService = adminService;
        this.pdfGenerator = pdfGenerator;
        this.customerService = customerService;
        this.applicationStatusHistoryService = applicationStatusHistoryService;
        this.statisticsService = statisticsService;
        this.downloadTokenService = downloadTokenService;
        this.euriborService = euriborService;
        this.messageSender = messageSender;
        this.agreementService = agreementService;
        this.emailNotificationService = emailNotificationService;
    }

    public Application findApplicationById(long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationException("No such application found"));
    }

    @Transactional
    public ApplicationResponseAdminDetails createApplication(ApplicationRequest applicationRequest) {
        try {
            Application application = new Application();
            populateNewApplicationWithRequest(applicationRequest, application);
            applicationRepository.save(application);
            applicationStatusHistoryService.addApplicationStatusHistory(application);
            sendNotification(application, "Application Created. ", "Your application was successfully created");
            return ApplicationMapper.toAdminDetailsResponse(application);
        } catch (DataAccessException dae) {
            throw new ApplicationException("Failed to save application data", dae);
        } catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while creating the application", e);
        }
    }

    @Transactional
    public ApplicationResponseAdminDetails updateApplication(long id, ApplicationRequest applicationRequest) {
        try {
            Application application = findApplicationById(id);
            populateExistingApplicationWithRequest(applicationRequest, application);
            applicationRepository.save(application);
            applicationStatusHistoryService.addApplicationStatusHistory(application);
            sendNotification(application, "Application Update", "Your application has been updated successfully.");
            return ApplicationMapper.toAdminDetailsResponse(application);
        } catch (DataAccessException dae) {
            throw new ApplicationException("Failed to save application data", dae);
        } catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while updating the application", e);
        }
    }

    @Transactional
    public ApplicationResponseFromAdmin updateApplicationFromAdmin(long applicationId, ApplicationRequestFromAdmin applicationRequest) {
        try {
            String uid = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            Application application = findApplicationById(applicationId);
            ApplicationStatus currentStatus = application.getStatus();
            Admin admin = adminService.findByUid(uid);
            if (application.getManager() == null) {
                throw new ApplicationException("Admin is not assigned to this application");
            }
            if (application.getManager().getId() != admin.getId()) {
                throw new ApplicationException("This admin is not assigned to this application");
            }

            ApplicationMapper.toEntityFromAdmin(application, applicationRequest, admin);
            applicationRepository.save(application);
            if(currentStatus != applicationRequest.getApplicationStatus()) {
                applicationStatusHistoryService.addApplicationStatusHistory(application);
            }
            sendNotification(application, "Application Update By Admin", "Your application has been updated by admin.");
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
    public String assignAdmin(long applicationId) throws ApplicationStatusHistoryException {
        String uid = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Application application = findApplicationById(applicationId);

        if (application.getManager() != null) {
            throw new ApplicationException("Application is already assigned to a manager");
        }
        Admin admin = adminService.findByUid(uid);
        admin.getAssignedApplications().add(application);
        application.setManager(admin);
        application.setStatus(ApplicationStatus.UNDER_REVIEW);
        applicationStatusHistoryService.addApplicationStatusHistory(application);
        applicationRepository.save(application);
        return "Admin " + application.getManager().getSurname() + " is successfully assigned to: " + application.getId();
    }

    @Transactional
    public String reAssignAdmin(String newAdminUid, long applicationId) {
        String uid = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Admin currentAdmin = adminService.findByUid(uid);
        Admin newAdmin = adminService.findByUid(newAdminUid);
        Application application = findApplicationById(applicationId);

        Comment comment = new Comment();
        comment.setApplication(application);
        comment.setAdmin(currentAdmin);
        comment.setText("The application was reassigned to admin: " + newAdmin.getId() + " by: " + currentAdmin.getId());
        comment.setCreatedAt(LocalDateTime.now());

        application.setManager(newAdmin);
        application.getComments().add(comment);
        newAdmin.getAssignedApplications().add(application);
        currentAdmin.getAssignedApplications().remove(application);
        try {
            emailService.notify(
                    "New application assigned",
                    "Dear manager, \n Admin:  "
                            + currentAdmin.getName() + " " + currentAdmin.getSurname() + " "
                            +currentAdmin.getEmail() + " reassigned to you a new application: "
                            + application.getId(),
                    currentAdmin.getEmail());
        }catch (Exception ignore){}
        applicationRepository.save(application);
        return "Application was successfully reassigned to: " + newAdmin.getId();
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
        } else {
            for (Application app : customer.getApplications()) {
                if (app.getStatus() != ApplicationStatus.CANCELLED
                        && app.getStatus() != ApplicationStatus.SIGNED
                        && app.getStatus() != ApplicationStatus.REJECTED) {
                    throw new ApplicationException("One user can not have more than one pending application at a time");
                }
            }
        }
        return customer;
    }

    private void sendNotification(Application application, String subject, String message) throws EmailServiceException, EmailServiceException {
        emailNotificationService.notify(subject, message, application.getCustomer().getEmail());
        try {
            messageSender.sendMessage(subject + "Please check your email.", application.getCustomer().getPhone());
        } catch (ApiException e) {
            throw new ApplicationException("Notification sending failed", e);
        }
    }

    private void populateCommonApplicationDetails(ApplicationRequest applicationRequest, Application application, Customer customer, VehicleDetails vehicleDetails, FinancialInfo financialInfo) {
        FinancialInfoMapper.toEntity(financialInfo, applicationRequest.getFinancialInfo(), application);
        VehicleMapper.toEntity(vehicleDetails, applicationRequest.getVehicleDetails(), application);
        String term = applicationRequest.getEuribor();
        euriborService.fetchEuriborRates(term).subscribe(rate -> {
            ApplicationMapper.toEntity(application, applicationRequest, customer, financialInfo, vehicleDetails, rate);
            System.out.println(rate);
        }, error -> {
            System.err.println("Error: " + error.getMessage());
        });
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

    public void updateApplicationStatus(long id, ApplicationStatus status) throws ApplicationStatusHistoryException, EmailServiceException {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found", new Throwable("ID: " + id)));
        application.setStatus(status);
        applicationRepository.save(application);
        applicationStatusHistoryService.addApplicationStatusHistory(application);
        if (application.getStatus() == ApplicationStatus.WAITING_FOR_SIGNING) {
            String baseUrl = "https://vrroom-backend.onrender.com";
            String token = downloadTokenService.generateToken(application);
            String encodedAgreementUrl = UrlBuilder.createEncodedUrl(baseUrl, "applications", token, "agreement");
            sendNotification(application, "Application Approved", "Your application has been approved successfully. Please click here to download your agreement: " + encodedAgreementUrl);
        }
    }

    public void saveSignedAgreement(Long id, MultipartFile signedAgreement) throws ApplicationStatusHistoryException, AgreementException, AgreementException {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found", new Throwable("ID: " + id)));
        application.setStatus(ApplicationStatus.SIGNED);
        applicationRepository.save(application);
        agreementService.saveAgreement(signedAgreement, application);
        applicationStatusHistoryService.addApplicationStatusHistory(application);
    }


}


