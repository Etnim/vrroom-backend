package com.vrrom.application.service;

import com.vrrom.application.exception.ApplicationException;
import com.vrrom.application.mapper.ApplicationMapper;
import com.vrrom.application.mapper.ApplicationListDTOMapper;
import com.vrrom.application.dto.ApplicationListDTO;
import com.vrrom.application.model.Application;
import com.vrrom.application.dto.ApplicationDTO;
import com.vrrom.application.model.ApplicationSortParameters;
import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.application.repository.ApplicationRepository;
import com.vrrom.customer.Customer;
import com.vrrom.customer.mappers.CustomerMapper;
import com.vrrom.email.service.EmailService;
import com.vrrom.financialInfo.mapper.FinancialInfoMapper;
import com.vrrom.financialInfo.model.FinancialInfo;
import com.vrrom.application.util.ApplicationSpecifications;
import com.vrrom.validation.ValidationService;
import com.vrrom.vehicle.mapper.VehicleMapper;
import com.vrrom.vehicle.model.VehicleDetails;
import jakarta.xml.bind.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.MailException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final EmailService emailService;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, EmailService emailService) {
        this.applicationRepository = applicationRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void createApplication(ApplicationDTO applicationDTO) {
        try {
            Application application = new Application();
            Customer customer = CustomerMapper.toEntity(applicationDTO.getCustomer(), application);
            FinancialInfo financialInfo = FinancialInfoMapper.toEntity(applicationDTO.getFinancialInfo(), application);
            List<VehicleDetails> vehicleDetails = VehicleMapper.toEntityList(applicationDTO.getVehicleDetails(), application);
            ApplicationMapper.toEntity(
                    application,
                    applicationDTO,
                    customer,
                    financialInfo,
                    vehicleDetails);
            applicationRepository.save(application);
            emailService.sendEmail("vrroom.leasing@gmail.com", application.getCustomer().getEmail(), "Application", "Your application has been created successfully.");
        } catch (DataAccessException dae) {
            throw new ApplicationException("Failed to save application data", dae);
        } catch (MailException me) {
            throw new ApplicationException("Failed to send notification email", me);
        } catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while creating the application", e);
        }
    }

    public Page<ApplicationListDTO> findPaginatedApplications(int pageNo, int pageSize, ApplicationSortParameters sortField, String sortDir, Long managerId, ApplicationStatus status, LocalDate startDate, LocalDate endDate) throws ValidationException {
        System.out.println(sortDir + " " + sortField + " " + startDate + " " + endDate);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortField.getDisplayName());
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
        Specification<Application> spec = Specification.where(null);
        if (managerId != null) {
            spec = spec.and(ApplicationSpecifications.hasManager(managerId));
        }
        if (status != null) {
            spec = spec.and(ApplicationSpecifications.hasStatus(status));
        }
        if (startDate != null || endDate != null) {
            if (ValidationService.isValidDateRange(startDate, endDate)) {
                spec = spec.and(ApplicationSpecifications.isCreatedBetween(startDate, endDate));
            }else {
               throw new IllegalArgumentException(new Throwable("Invalid date range"));
            }
        }
        Page<Application> page = applicationRepository.findAll(spec, paging);
        return page.map(ApplicationListDTOMapper::toApplicationListDTO);
    }
}


