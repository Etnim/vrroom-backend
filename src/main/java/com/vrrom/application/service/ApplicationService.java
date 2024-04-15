package com.vrrom.application.service;

import com.vrrom.application.model.Application;
import com.vrrom.application.model.ApplicationDTO;
import com.vrrom.application.repository.ApplicationRepository;
import com.vrrom.customer.Customer;
import com.vrrom.customer.CustomerDTO;
import com.vrrom.financialInfo.model.FinancialInfo;
import com.vrrom.financialInfo.model.FinancialInfoDTO;
import com.vrrom.vehicle.model.VehicleDTO;
import com.vrrom.vehicle.model.VehicleDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Transactional
    public Application createApplication(ApplicationDTO applicationDTO) {
        Customer customer = convertToEntity(applicationDTO.getCustomer());
        FinancialInfo financialInfo = convertToEntity(applicationDTO.getFinancialInfo());
        List<VehicleDetails> vehicleDetails = convertToEntity(applicationDTO.getVehicleDetails());

        Application application = new Application();
        application.setCustomer(customer);
        application.setFinancialInfo(financialInfo);
        application.setVehicleDetails(vehicleDetails);
        application.setPrice(applicationDTO.getPrice());
        application.setResidualValue(applicationDTO.getResidualValue());
        application.setYearPeriod(applicationDTO.getYearPeriod());

        return applicationRepository.save(application);
    }

    private List<VehicleDetails> convertToEntity(List<VehicleDTO> vehicleDTOS) {
        List<VehicleDetails> vehicles = new ArrayList<>();

        for (VehicleDTO vehicleDTO : vehicleDTOS) {
            VehicleDetails vehicleDetails = new VehicleDetails();

            vehicleDetails.setEmission(vehicleDTO.getEmission());
            vehicleDetails.setFuel(vehicleDTO.getFuel());
            vehicleDetails.setYear(vehicleDTO.getYear());
            vehicleDetails.setBrand(vehicleDTO.getBrand());
            vehicleDetails.setModel(vehicleDTO.getModel());

            vehicles.add(vehicleDetails);
        }
        return vehicles;
    }

    private FinancialInfo convertToEntity(FinancialInfoDTO financialInfoDTO) {
        FinancialInfo financialInfo = new FinancialInfo();

        financialInfo.setMonthlyIncome(financialInfoDTO.getMonthlyIncome());
        financialInfo.setDependants(financialInfoDTO.getDependants());
        financialInfo.setMaritalStatus(financialInfoDTO.getMaritalStatus());
        financialInfo.setMonthlyObligations(financialInfoDTO.getMonthlyObligations());

        return financialInfo;
    }

    private Customer convertToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();

        customer.setName(customerDTO.getName());
        customer.setSurname(customerDTO.getSurname());
        customer.setEmail(customerDTO.getEmail());
        customer.setBirthDate(customerDTO.getBirthDate());
        customer.setPhone(customerDTO.getPhone());
        customer.setAddress(customerDTO.getAddress());

        return customer;
    }
}
