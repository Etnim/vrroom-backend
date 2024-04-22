package com.vrrom.application.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vrrom.application.dtos.ApplicationRequest;
import com.vrrom.customer.Customer;
import com.vrrom.admin.Admin;
import com.vrrom.financialInfo.model.FinancialInfo;
import com.vrrom.vehicle.model.VehicleDetails;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "financial_info_id")
    private FinancialInfo financialInfo;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @JsonManagedReference
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VehicleDetails> vehicleDetails;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Admin manager;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "down_payment")
    private BigDecimal downPayment;

    @Column(name = "residual_value")
    private int residualValue;

    @Column(name = "year_period")
    private int yearPeriod;

    @Column(name = "interest_rate")
    private double interestRate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    public static class ApplicationBuilder {
        private List<VehicleDetails> vehicleDetails = new ArrayList<>();

        public ApplicationBuilder defaultCreatedAtNow() {
            this.createdAt = LocalDate.now();
            return this;
        }

        public ApplicationBuilder defaultUpdatedAtNow() {
            this.updatedAt = LocalDate.now();
            return this;
        }

        public ApplicationBuilder calculateInterestRate() {
            if (this.customer != null) {
                this.interestRate = 2.0 + (this.customer.getCreditRating() - 1) * 1.5;
            }
            return this;
        }

        public ApplicationBuilder setStatusSubmitted() {
            this.status = ApplicationStatus.SUBMITTED;
            return this;
        }

        public ApplicationBuilder fromDto(ApplicationRequest dto) {
            this.price = dto.getPrice();
            this.residualValue = dto.getResidualValue();
            this.yearPeriod = dto.getYearPeriod();
            return this;
        }

        public Application build() {
            defaultCreatedAtNow();
            defaultUpdatedAtNow();
            calculateInterestRate();
            setStatusSubmitted();
            Application application = new Application();
            application.setId(this.id);
            application.setCustomer(this.customer);
            application.setFinancialInfo(this.financialInfo);
            application.setVehicleDetails(this.vehicleDetails);
            application.setManager(this.manager);
            application.setPrice(this.price);
            application.setDownPayment(this.downPayment);
            application.setResidualValue(this.residualValue);
            application.setYearPeriod(this.yearPeriod);
            application.setInterestRate(this.interestRate);
            application.setStatus(this.status);
            application.setCreatedAt(this.createdAt);
            application.setUpdatedAt(this.updatedAt);
            return application;
        }
    }
}
