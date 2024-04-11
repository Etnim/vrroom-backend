package com.vrrom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    @Id
    @Column(name = "id")
    private long id;

    @PrimaryKeyJoinColumn(name = "financial_info")
    @OneToOne
    private FinancialInfo financialInfo;

    @PrimaryKeyJoinColumn(name = "customer")
    @OneToOne
    private Customer customer;

    @Column(name = "vehicle_details")
    @OneToMany(mappedBy = "application")
    private List<VehicleDetails> vehicleDetails;

    @PrimaryKeyJoinColumn(name = "manager")
    @ManyToOne
    private Admin manager;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "down_payment")
    private int downPayment;

    @Column(name = "residual_value")
    private int residualValue;

    @Column(name = "year_period")
    private int yearPeriod;

    @Column(name = "interest_rate")
    private double interestRate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AppStatus status;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;


}
