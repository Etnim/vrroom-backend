package com.vrrom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDetails {
    @Id
    @Column(name = "id")
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

    @Column(name = "emission")
    @Enumerated(EnumType.STRING)
    private Emission emission;

    @PrimaryKeyJoinColumn(name = "application")
    @ManyToOne
    private Application application;

}
