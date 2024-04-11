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

    @Column(name = "monthlyIncome")
    private BigDecimal monthlyIncome;

    @Column(name = "monthlyObligations")
    private BigDecimal monthlyObligations;

    @Column(name = "maritalStatus")
    private MaritalStatus maritalStatus;

    @Column(name = "dependants")
    private int dependants;
}
