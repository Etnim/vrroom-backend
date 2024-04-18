package com.vrrom.application.service;

import com.vrrom.application.exception.ApplicationException;
import com.vrrom.application.model.AppStatus;
import com.vrrom.application.model.Application;
import com.vrrom.application.model.ApplicationDTO;
import com.vrrom.application.repository.ApplicationRepository;
import com.vrrom.customer.Customer;
import com.vrrom.customer.dtos.CustomerDTO;
import com.vrrom.customer.mappers.CustomerMapper;
import com.vrrom.email.service.EmailService;
import com.vrrom.financialInfo.mapper.FinancialInfoMapper;
import com.vrrom.financialInfo.model.FinancialInfo;
import com.vrrom.financialInfo.model.FinancialInfoDTO;
import com.vrrom.vehicle.mapper.VehicleMapper;
import com.vrrom.vehicle.model.VehicleDTO;
import com.vrrom.vehicle.model.VehicleDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

            application.setCustomer(customer);
            application.setFinancialInfo(financialInfo);
            application.setVehicleDetails(vehicleDetails);
            application.setPrice(applicationDTO.getPrice());
            application.setResidualValue(applicationDTO.getResidualValue());
            application.setYearPeriod(applicationDTO.getYearPeriod());
            application.setCreatedAt(LocalDate.now());
            application.setUpdatedAt(LocalDate.now());
            application.setInterestRate(application.calculateInterestRate());
            application.setStatus(AppStatus.SUBMITTED);

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
}
