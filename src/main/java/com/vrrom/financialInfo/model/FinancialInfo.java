package com.vrrom.financialInfo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FinancialInfo {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "monthly_income")
    private BigDecimal monthlyIncome;

    @Column(name = "monthly_obligations")
    private BigDecimal monthlyObligations;

    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @Column(name = "dependants")
    private int dependants;
}
