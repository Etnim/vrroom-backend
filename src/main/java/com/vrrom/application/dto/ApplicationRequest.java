package com.vrrom.application.dto;

import com.vrrom.customer.dtos.CustomerRequest;
import com.vrrom.financialInfo.dtos.FinancialInfoRequest;
import com.vrrom.vehicle.dtos.VehicleRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequest {
    @Valid
    private CustomerRequest customer;

    @Valid
    private VehicleRequest vehicleDetails;

    @Valid
    private FinancialInfoRequest financialInfo;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "8000", message = "Price must be greater than 8000")
    @DecimalMax(value = "120000", message = "Price must be less than 120,000")
    private BigDecimal price;

    @Min(value = 10, message = "Minimal value for down Payment is 10")
    @Max(value = 60, message = "Maximum value for down Payment is 60")
    private int downPayment;

    @Min(value = 0, message = "Minimal value for residual Value is 0")
    @Max(value = 30, message = "Maximum value for residual Value is 30")
    private int residualValue;

    @Min(value = 1, message = "Year period must be at least 1 year")
    @Max(value = 7, message = "Year period must be less than 7")
    private int yearPeriod;

    @Pattern(regexp = "^(3m|6m)$", message = "Euribor must be either '3m' or '6m'")
    private String euribor;
}
