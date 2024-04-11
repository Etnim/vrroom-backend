package com.vrrom.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Builder
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
