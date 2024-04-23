package com.vrrom.financialInfo.dtos;

import com.vrrom.financialInfo.model.EmploymentStatus;
import com.vrrom.financialInfo.model.MaritalStatus;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FinancialInfoResponse {
    private BigDecimal monthlyIncome;
    private MaritalStatus maritalStatus;
    private int dependants;
    private BigDecimal monthlyObligations;
    private BigDecimal disposableIncome;
}
