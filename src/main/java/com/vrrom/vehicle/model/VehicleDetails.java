package com.vrrom.vehicle.model;

import com.vrrom.application.model.Application;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "brand")
    private String brand;
    @Column(name = "model")
    private String model;
    @Column(name = "year")
    private int year;
    @Column(name = "fuel")
    @Enumerated(EnumType.STRING)
    private FuelType fuel;
    @Column(name = "emission_start")
    private int emissionStart;
    @Column(name = "emission_end")
    private int emissionEnd;
    @OneToOne
    @JoinColumn(name = "application_id")
    private Application application;
}
