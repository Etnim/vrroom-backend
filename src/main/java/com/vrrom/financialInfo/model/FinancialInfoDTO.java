package com.vrrom.financialInfo.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FinancialInfoDTO {
    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly income must be greater than 0")
    private BigDecimal monthlyIncome;

    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly obligations must be greater than 0")
    private BigDecimal monthlyObligations;

    @NotNull(message = "Marital status must not be null")
    private MaritalStatus maritalStatus;

    @Min(value = 0, message = "Number of dependants must be non-negative")
    private int dependants;
}
