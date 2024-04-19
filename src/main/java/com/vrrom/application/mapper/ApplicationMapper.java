package com.vrrom.application.mapper;


import com.vrrom.application.model.Application;
import com.vrrom.application.model.ApplicationDTO;
import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.customer.Customer;
import com.vrrom.financialInfo.model.FinancialInfo;
import com.vrrom.vehicle.model.VehicleDetails;
import com.vrrom.application.model.Application;
import com.vrrom.application.model.ApplicationDTO;
import com.vrrom.customer.Customer;
import com.vrrom.financialInfo.model.FinancialInfo;
import com.vrrom.vehicle.model.VehicleDetails;
import java.time.LocalDate;
import java.util.List;

public class ApplicationMapper {
    public static Application toEntity( Application application, ApplicationDTO applicationDTO, Customer customer, FinancialInfo financialInfo, List<VehicleDetails> vehicleDetails) {
        application.setCustomer(customer);
        application.setFinancialInfo(financialInfo);
        application.setVehicleDetails(vehicleDetails);
        application.setPrice(applicationDTO.getPrice());
        application.setResidualValue(applicationDTO.getResidualValue());
        application.setYearPeriod(applicationDTO.getYearPeriod());
        application.setCreatedAt(LocalDate.now());
        application.setUpdatedAt(LocalDate.now());
        application.setInterestRate(application.calculateInterestRate());
        application.setStatus(ApplicationStatus.SUBMITTED);
        return application;
    }
}
