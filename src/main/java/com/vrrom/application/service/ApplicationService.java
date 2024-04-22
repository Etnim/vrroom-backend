package com.vrrom.application.service;

import com.vrrom.application.dtos.ApplicationRequest;
import com.vrrom.application.dtos.ApplicationResponse;
import com.vrrom.application.exception.ApplicationException;
import com.vrrom.application.mapper.ApplicationListDTOMapper;
import com.vrrom.application.mapper.ApplicationMapper;
import com.vrrom.application.model.Application;
import com.vrrom.application.dtos.ApplicationListDTO;
import com.vrrom.application.repository.ApplicationRepository;
import com.vrrom.customer.Customer;
import com.vrrom.customer.mappers.CustomerMapper;
import com.vrrom.email.service.EmailService;
import com.vrrom.financialInfo.mapper.FinancialInfoMapper;
import com.vrrom.financialInfo.model.FinancialInfo;
import com.vrrom.vehicle.mapper.VehicleMapper;
import com.vrrom.vehicle.model.VehicleDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public ApplicationResponse createApplication(ApplicationRequest applicationRequest) {
        try {
            Application application = new Application();
            Customer customer = CustomerMapper.toEntity(applicationRequest.getCustomer(), application);
            FinancialInfo financialInfo = FinancialInfoMapper.toEntity(applicationRequest.getFinancialInfo(), application);
            List<VehicleDetails> vehicleDetails = VehicleMapper.toEntityList(applicationRequest.getVehicleDetails(), application);
            ApplicationMapper.toEntity(
                    application,
                    applicationRequest,
                    customer,
                    financialInfo,
                    vehicleDetails);
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

    public Page<ApplicationListDTO> findPaginatedApplications(int pageNo, int pageSize, String sortField, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir.toUpperCase()), sortField);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
        Page<Application> page = applicationRepository.findAll(paging);
        return page.map(application -> ApplicationListDTOMapper.toApplicationListDTO(application, application.getManager()));
    }

    @Transactional
    public ApplicationResponse findApplicationById(long id) {
        Optional<Application> application = applicationRepository.findById(id);
        return ApplicationMapper.toResponse(application.orElseThrow());
    }

}


