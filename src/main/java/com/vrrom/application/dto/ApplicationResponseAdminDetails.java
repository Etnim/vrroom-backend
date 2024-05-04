package com.vrrom.application.dto;

import com.vrrom.admin.dtos.AdminDTO;
import com.vrrom.comment.Comment;
import com.vrrom.comment.CommentResponse;
import com.vrrom.customer.dtos.CustomerResponse;
import com.vrrom.financialInfo.dtos.FinancialInfoResponse;
import com.vrrom.vehicle.dtos.VehicleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponseAdminDetails {
    private long applicationID;
    private String applicationStatus;
    private LocalDateTime dateOfSubmission;
    private AdminDTO assignedManager;
    private CustomerResponse customer;
    private VehicleResponse vehicleDetails;
    private FinancialInfoResponse financialInfo;
    private BigDecimal price;
    private BigDecimal downPayment;
    private int yearPeriod;
    private BigDecimal residualValue;
    private double interestRate;
    private String euriborTerm;
    private double euribor;
    private BigDecimal agreementFee;
    private List<CommentResponse> comments;
}
