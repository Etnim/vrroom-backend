package com.vrrom.financialInfo.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class FinancialInfoDTO {
    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly income must be greater than 0")
    private BigDecimal monthlyIncome;

    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly obligations must be greater than 0")
    private BigDecimal monthlyObligations;

    @ElementCollection(targetClass = MaritalStatus.class)
    @Enumerated(EnumType.STRING)
    @NotNull
    private MaritalStatus maritalStatus;

    @ElementCollection(targetClass = EmploymentStatus.class)
    @Enumerated(EnumType.STRING)
    @NotNull
    private EmploymentStatus employmentStatus;

    @NotNull
    private int employmentTerm;

    @Min(value = 0, message = "Number of dependants must be non-negative")
    private int dependants;
}
