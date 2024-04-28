package com.vrrom.application.model;

import com.vrrom.admin.Admin;
import com.vrrom.comment.Comment;
import com.vrrom.customer.Customer;
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
import java.time.LocalDateTime;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "financial_info_id")
    private FinancialInfo financialInfo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL)
    private VehicleDetails vehicleDetails;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    private Admin manager;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "down_payment")

    private BigDecimal downPayment;

    @Column(name = "residual_value")
    private BigDecimal residualValue;

    @Column(name = "year_period")
    private int yearPeriod;

    @Column(name = "interest_rate")
    private double interestRate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    @Column(name = "agreement_fee")
    private BigDecimal agreementFee;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;
}
