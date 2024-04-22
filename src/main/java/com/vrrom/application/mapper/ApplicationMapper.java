package com.vrrom.application.mapper;

import com.vrrom.application.model.Application;
import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.application.dtos.ApplicationResponse;
import com.vrrom.application.dtos.ApplicationRequest;
import com.vrrom.customer.Customer;
import com.vrrom.customer.dtos.CustomerResponse;
import com.vrrom.customer.mappers.CustomerMapper;
import com.vrrom.euribor.dto.EuriborRate;
import com.vrrom.financialInfo.dtos.FinancialInfoResponse;
import com.vrrom.financialInfo.mapper.FinancialInfoMapper;
import com.vrrom.financialInfo.model.FinancialInfo;
import com.vrrom.vehicle.dtos.VehicleResponse;
import com.vrrom.vehicle.mapper.VehicleMapper;
import com.vrrom.vehicle.model.VehicleDetails;
import java.time.LocalDate;
import java.util.List;

public class ApplicationMapper {
    public static Application toEntity(Application application, ApplicationRequest applicationRequest, Customer customer, FinancialInfo financialInfo, List<VehicleDetails> vehicleDetails) {
        application.setCustomer(customer);
        application.setFinancialInfo(financialInfo);
        application.setVehicleDetails(vehicleDetails);
        application.setPrice(applicationRequest.getPrice());
        application.setResidualValue(applicationRequest.getResidualValue());
        application.setYearPeriod(applicationRequest.getYearPeriod());
        application.setCreatedAt(LocalDate.now());
        application.setUpdatedAt(LocalDate.now());
        application.setInterestRate(application.calculateInterestRate());
        application.setStatus(ApplicationStatus.SUBMITTED);
        application.setDownPayment(application.calculateDownPayment());
        return application;
    }

    public static ApplicationResponse toResponse(Application application){
        CustomerResponse customer = CustomerMapper.toResponse(application.getCustomer());
        FinancialInfoResponse financialInfo = FinancialInfoMapper.toResponse(application.getFinancialInfo());
        List<VehicleResponse> vehicles = VehicleMapper.toResponseList(application.getVehicleDetails());
        EuriborRate euribor = new EuriborRate();

        ApplicationResponse response = new ApplicationResponse();
        response.setApplicationID(application.getId());
        response.setApplicationStatus(application.getStatus());
        response.setDateOfSubmission(application.getCreatedAt());
        response.setAssignedManager(application.getManager());
        response.setCustomer(customer);
        response.setFinancialInfo(financialInfo);
        response.setVehicleDetails(vehicles);
        response.setPrice(application.getPrice());
        response.setYearPeriod(application.getYearPeriod());
        response.setResidualValue(application.getResidualValue());
        response.setInterestRate(application.getInterestRate());
        response.setEuribor(euribor.getRate());
        response.setAgreementFee(application.calculateDownPayment());

        return response;
    }
}
