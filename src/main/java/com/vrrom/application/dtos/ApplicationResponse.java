package com.vrrom.application.dtos;

import com.vrrom.admin.Admin;
import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.customer.dtos.CustomerResponse;
import com.vrrom.financialInfo.dtos.FinancialInfoRequest;
import com.vrrom.financialInfo.dtos.FinancialInfoResponse;
import com.vrrom.vehicle.dtos.VehicleRequest;
import com.vrrom.vehicle.dtos.VehicleResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "id can not be null")
    private long applicationID;

    @NotBlank(message = "status can not be blank")
    private ApplicationStatus applicationStatus;

    @NotNull(message = "id can not be null")
    private LocalDate dateOfSubmission;

    private Admin assignedManager;
    @Valid
    private CustomerResponse customer;

    @Valid
    private List<VehicleResponse> vehicleDetails;

    @Valid
    private FinancialInfoResponse financialInfo;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    private BigDecimal price;

    @Min(value = 0, message = "Down payment cannot be negative")
    private int downPayment;
    @Min(value = 1, message = "Year period must be at least 1 year")
    private int yearPeriod;

    @Min(value = 0, message = "Residual value cannot be negative")
    private int residualValue;

    private double interestRate;
    private double euribor;
    private BigDecimal agreementFee;

}
