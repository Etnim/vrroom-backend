package com.vrrom.vehicle.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vrrom.application.model.Application;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;

    public static class Builder {
        private int emissionStart;
        private int emissionEnd;
        private FuelType fuel;
        private int year;
        private String brand;
        private String model;
        private Application application;

        public Builder withVehicleDTO(VehicleDTO vehicleDTO) {
            this.emissionStart = vehicleDTO.getEmissionStart();
            this.emissionEnd = vehicleDTO.getEmissionEnd();
            this.fuel = vehicleDTO.getFuel();
            this.year = vehicleDTO.getYear();
            this.brand = vehicleDTO.getBrand();
            this.model = vehicleDTO.getModel();
            return this;
        }

        public Builder withApplication(Application application) {
            this.application = application;
            return this;
        }

        public VehicleDetails build() {
            VehicleDetails vehicleDetails = new VehicleDetails();
            vehicleDetails.setEmissionStart(this.emissionStart);
            vehicleDetails.setEmissionEnd(this.emissionEnd);
            vehicleDetails.setFuel(this.fuel);
            vehicleDetails.setYear(this.year);
            vehicleDetails.setBrand(this.brand);
            vehicleDetails.setModel(this.model);
            vehicleDetails.setApplication(this.application);
            return vehicleDetails;
        }
    }
}
