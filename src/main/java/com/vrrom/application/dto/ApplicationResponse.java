package com.vrrom.application.dto;

import com.vrrom.admin.Admin;
import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.customer.dtos.CustomerResponse;
import com.vrrom.financialInfo.dtos.FinancialInfoResponse;
import com.vrrom.vehicle.dtos.VehicleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponse {
    private long applicationID;
    private ApplicationStatus applicationStatus;
    private LocalDate dateOfSubmission;
    private Admin assignedManager;
    private CustomerResponse customer;
    private List<VehicleResponse> vehicleDetails;
    private FinancialInfoResponse financialInfo;
    private BigDecimal price;
    private int downPayment;
    private int yearPeriod;
    private int residualValue;
    private double interestRate;
    private double euribor;
    private BigDecimal agreementFee;
}
