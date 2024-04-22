package com.vrrom.application.dto;

import com.vrrom.customer.dtos.CustomerDTO;
import com.vrrom.financialInfo.model.FinancialInfoDTO;
import com.vrrom.vehicle.model.VehicleDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDTO {
    @Valid
    private CustomerDTO customer;

    @Valid
    private List<VehicleDTO> vehicleDetails;

    @Valid
    private FinancialInfoDTO financialInfo;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    private BigDecimal price;

    @Min(value = 0, message = "Down payment cannot be negative")
    private int downPayment;

    @Min(value = 0, message = "Residual value cannot be negative")
    private int residualValue;

    @Min(value = 1, message = "Year period must be at least 1 year")
    private int yearPeriod;
}
