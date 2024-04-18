package com.vrrom.financialInfo.model;

import com.vrrom.application.model.Application;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "monthly_income")
    private BigDecimal monthlyIncome;

    @Column(name = "monthly_obligations")
    private BigDecimal monthlyObligations;

    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @Column(name = "employment_status")
    private EmploymentStatus employmentStatus;

    @Column(name = "employment_term")
    private int employmentTerm;

    @Column(name = "dependants")
    private int dependants;

    @OneToOne(mappedBy = "financialInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Application application;

    public static class Builder {
        private BigDecimal monthlyIncome;
        private int dependants;
        private MaritalStatus maritalStatus;
        private BigDecimal monthlyObligations;
        private Application application;

        public Builder withFinancialInfoDTO(FinancialInfoDTO financialInfoDTO) {
            this.monthlyIncome = financialInfoDTO.getMonthlyIncome();
            this.dependants = financialInfoDTO.getDependants();
            this.maritalStatus = financialInfoDTO.getMaritalStatus();
            this.monthlyObligations = financialInfoDTO.getMonthlyObligations();
            return this;
        }

        public Builder withApplication(Application application) {
            this.application = application;
            return this;
        }

        public FinancialInfo build() {
            FinancialInfo financialInfo = new FinancialInfo();
            financialInfo.setMonthlyIncome(this.monthlyIncome);
            financialInfo.setDependants(this.dependants);
            financialInfo.setMaritalStatus(this.maritalStatus);
            financialInfo.setMonthlyObligations(this.monthlyObligations);
            financialInfo.setApplication(this.application);
            return financialInfo;
        }
    }
}
