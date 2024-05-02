package com.vrrom.application.mapper;

import com.vrrom.admin.dtos.AdminDTO;
import com.vrrom.admin.mapper.AdminMapper;
import com.vrrom.admin.model.Admin;
import com.vrrom.application.calculator.Calculator;
import com.vrrom.application.dto.ApplicationRequest;
import com.vrrom.application.dto.ApplicationRequestFromAdmin;
import com.vrrom.application.dto.ApplicationResponseAdminDetails;
import com.vrrom.application.dto.ApplicationResponseFromAdmin;
import com.vrrom.application.model.Application;
import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.comment.Comment;
import com.vrrom.comment.CommentMapper;
import com.vrrom.customer.dtos.CustomerResponse;
import com.vrrom.customer.mappers.CustomerMapper;
import com.vrrom.customer.model.Customer;
import com.vrrom.financialInfo.dtos.FinancialInfoResponse;
import com.vrrom.financialInfo.mapper.FinancialInfoMapper;
import com.vrrom.financialInfo.model.FinancialInfo;
import com.vrrom.vehicle.dtos.VehicleResponse;
import com.vrrom.vehicle.mapper.VehicleMapper;
import com.vrrom.vehicle.model.VehicleDetails;

import java.time.LocalDateTime;

public class ApplicationMapper {
    public static void toEntity(Application application, ApplicationRequest applicationRequest, Customer customer, FinancialInfo financialInfo, VehicleDetails vehicleDetails, double euribor) {
        Calculator calculator = new Calculator();

        application.setCustomer(customer);
        application.setFinancialInfo(financialInfo);
        application.setVehicleDetails(vehicleDetails);
        application.setPrice(applicationRequest.getPrice());
        application.setResidualValue(calculator.getResidualValue(applicationRequest));
        application.setYearPeriod(applicationRequest.getYearPeriod());
        application.setInterestRate(calculator.getInterestRate(customer));
        application.setAgreementFee(calculator.getAgreementFee(applicationRequest.getPrice()));
        application.setDownPayment(calculator.getDownPayment(applicationRequest));
        application.setMonthlyPayment(calculator.getMonthlyPayment(applicationRequest, customer, euribor));
        application.setEuribor(euribor);
        application.setCreatedAt(LocalDateTime.now());
        application.setUpdatedAt(LocalDateTime.now());
        application.setStatus(ApplicationStatus.SUBMITTED);
    }

    public static void toEntityFromAdmin(Application application, ApplicationRequestFromAdmin applicationFromAdmin, Admin admin) {
        application.setInterestRate(applicationFromAdmin.getInterestRate());
        application.setAgreementFee(applicationFromAdmin.getAgreementFee());
        application.setStatus(application.getStatus());

        if(applicationFromAdmin.getComment() != null) {
            Comment comment = CommentMapper.toEntity(applicationFromAdmin.getComment(), application, admin);
            application.getComments().add(comment);
        }
    }

    public static ApplicationResponseFromAdmin toResponseFromAdmin(Application application){
        return new ApplicationResponseFromAdmin(
                application.getInterestRate(),
                application.getAgreementFee(),
                application.getStatus(),
                CommentMapper.toCommentResponses(application.getComments()));
    }

    public static ApplicationResponseAdminDetails toResponse(Application application) {
        CustomerResponse customer = CustomerMapper.toResponse(application.getCustomer());
        FinancialInfoResponse financialInfo = FinancialInfoMapper.toResponse(application.getFinancialInfo());
        VehicleResponse vehicles = VehicleMapper.toResponse(application.getVehicleDetails());
        AdminDTO admin = AdminMapper.toDTO(application.getManager());

        ApplicationResponseAdminDetails response = new ApplicationResponseAdminDetails();
        response.setApplicationID(application.getId());
        response.setApplicationStatus(application.getStatus().getApplicationStatusText());
        response.setDateOfSubmission(application.getCreatedAt());
        response.setAssignedManager(admin);
        response.setCustomer(customer);
        response.setFinancialInfo(financialInfo);
        response.setVehicleDetails(vehicles);
        response.setPrice(application.getPrice());
        response.setYearPeriod(application.getYearPeriod());
        response.setInterestRate(application.getInterestRate());
        response.setDownPayment(application.getDownPayment());
        response.setResidualValue(application.getResidualValue());
        response.setEuribor(application.getEuribor());
        response.setAgreementFee(application.getAgreementFee());
        return response;
    }
}
