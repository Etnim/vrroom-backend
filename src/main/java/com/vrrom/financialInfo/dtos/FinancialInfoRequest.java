package com.vrrom.financialInfo.dtos;

import com.vrrom.financialInfo.model.EmploymentStatus;
import com.vrrom.financialInfo.model.EmploymentTerm;
import com.vrrom.financialInfo.model.MaritalStatus;
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
public class FinancialInfoRequest {
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
    @Enumerated(EnumType.STRING)
    @NotNull
    private EmploymentTerm employmentTerm;

    @Min(value = 0, message = "Number of dependants must be non-negative")
    private int dependants;
}
