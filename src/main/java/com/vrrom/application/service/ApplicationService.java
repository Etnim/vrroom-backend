package com.vrrom.application.service;

import com.vrrom.application.model.AppStatus;
import com.vrrom.application.model.Application;
import com.vrrom.application.model.ApplicationDTO;
import com.vrrom.application.repository.ApplicationRepository;
import com.vrrom.customer.Customer;
import com.vrrom.customer.CustomerDTO;
import com.vrrom.email.service.EmailService;
import com.vrrom.financialInfo.model.FinancialInfo;
import com.vrrom.financialInfo.model.FinancialInfoDTO;
import com.vrrom.vehicle.model.VehicleDTO;
import com.vrrom.vehicle.model.VehicleDetails;
import org.springframework.beans.factory.annotation.Autowired;
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
        Application application = new Application();

        Customer customer = convertToEntity(applicationDTO.getCustomer(), application);
        FinancialInfo financialInfo = convertToEntity(applicationDTO.getFinancialInfo(), application);
        List<VehicleDetails> vehicleDetails = convertToEntity(applicationDTO.getVehicleDetails(), application);

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
//
//       try{
//           applicationRepository.save(application);
//           emailService.sendEmail("", "","","");
//       }catch (Exception e){
//
//       }
    }

    private List<VehicleDetails> convertToEntity(List<VehicleDTO> vehicleDTOS, Application application) {
        List<VehicleDetails> vehicles = new ArrayList<>();

        for (VehicleDTO vehicleDTO : vehicleDTOS) {
            VehicleDetails vehicleDetails = new VehicleDetails();
            vehicleDetails.setEmission(vehicleDTO.getEmission());
            vehicleDetails.setFuel(vehicleDTO.getFuel());
            vehicleDetails.setYear(vehicleDTO.getYear());
            vehicleDetails.setBrand(vehicleDTO.getBrand());
            vehicleDetails.setModel(vehicleDTO.getModel());
            vehicleDetails.setApplication(application);

            vehicles.add(vehicleDetails);
        }
        return vehicles;
    }

    private FinancialInfo convertToEntity(FinancialInfoDTO financialInfoDTO, Application application) {
        FinancialInfo financialInfo = new FinancialInfo();

        financialInfo.setMonthlyIncome(financialInfoDTO.getMonthlyIncome());
        financialInfo.setDependants(financialInfoDTO.getDependants());
        financialInfo.setMaritalStatus(financialInfoDTO.getMaritalStatus());
        financialInfo.setMonthlyObligations(financialInfoDTO.getMonthlyObligations());
        financialInfo.setApplication(application);

        return financialInfo;
    }

    private Customer convertToEntity(CustomerDTO customerDTO, Application application) {
        String name = customerDTO.getName();
        String surname = customerDTO.getSurname();
        String email = customerDTO.getEmail();
        Date birthDate = customerDTO.getBirthDate();
        String phone = customerDTO.getPhone();
        String address = customerDTO.getAddress();

        Customer customer = new Customer(name, surname, birthDate, email, phone, address);
        customer.setApplication(application);
        return customer;
    }
}
