package com.vrrom.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.vrrom.application.exception.ApplicationException;
import com.vrrom.application.service.ApplicationService;
import com.vrrom.customer.dtos.CustomerDTO;
import com.vrrom.financialInfo.model.EmploymentStatus;
import com.vrrom.financialInfo.model.FinancialInfoDTO;
import com.vrrom.financialInfo.model.MaritalStatus;
import com.vrrom.vehicle.model.FuelType;
import com.vrrom.vehicle.model.VehicleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailException;

import com.vrrom.application.model.ApplicationDTO;
import com.vrrom.application.repository.ApplicationRepository;
import com.vrrom.email.service.EmailService;
import org.springframework.mail.MailSendException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ApplicationService applicationService;

    private ApplicationDTO applicationDTO;

    @BeforeEach
    void setUp() {
        CustomerDTO customerDTO = new CustomerDTO(
                1234567890,
                "name",
                "surname",
                "mail@gmail.com",
                new Date(),
                "+370123456789",
                "address"
        );

        FinancialInfoDTO financialInfoDTO = new FinancialInfoDTO(
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(1000),
                MaritalStatus.SINGLE,
                EmploymentStatus.FULLTIME,
                1,
                0
        );

        VehicleDTO vehicleDTO = new VehicleDTO(
                "brand",
                "model",
                2,
                FuelType.DIESEL,
                0,
                20
                );

        List<VehicleDTO> vehicles = new ArrayList<>();
        vehicles.add(vehicleDTO);

        applicationDTO = new ApplicationDTO(
                customerDTO,
                vehicles,
                financialInfoDTO,
                BigDecimal.valueOf(100),
                40,
                40,
                2
                );

    }

    @Test
    void createApplication_ShouldProcessCorrectly_WhenNoExceptions() {
        applicationService.createApplication(applicationDTO);
        verify(applicationRepository).save(any());
        verify(emailService).sendEmail(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void createApplication_ShouldThrowApplicationException_WhenDataAccessFails() {
        doThrow(DataIntegrityViolationException.class).when(applicationRepository).save(any());
        assertThrows(ApplicationException.class, () -> applicationService.createApplication(applicationDTO));
        }

    @Test
    void createApplication_ShouldThrowApplicationException_WhenEmailFails() {
        doThrow(MailSendException.class).when(emailService).sendEmail(anyString(), anyString(), anyString(), anyString());
        assertThrows(ApplicationException.class, () -> applicationService.createApplication(applicationDTO));

    }

    @Test
    void createApplication_ShouldThrowApplicationException_OnUnexpectedException() {
        doThrow(RuntimeException.class).when(applicationRepository).save(any());
        assertThrows(ApplicationException.class, () -> applicationService.createApplication(applicationDTO));
    }
}