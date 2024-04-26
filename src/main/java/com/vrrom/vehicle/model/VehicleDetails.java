package com.vrrom.vehicle.model;

import com.vrrom.application.model.Application;
import com.vrrom.vehicle.dtos.VehicleRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @OneToOne(mappedBy ="vehicleDetails", cascade = CascadeType.ALL)

    private Application application;

    public static class Builder {
        private int emissionStart;
        private int emissionEnd;
        private FuelType fuel;
        private int year;
        private String brand;
        private String model;
        private Application application;

        public Builder withVehicleDTO(VehicleRequest vehicleRequest) {
            this.emissionStart = vehicleRequest.getEmissionStart();
            this.emissionEnd = vehicleRequest.getEmissionEnd();
            this.fuel = vehicleRequest.getFuel();
            this.year = vehicleRequest.getYear();
            this.brand = vehicleRequest.getBrand();
            this.model = vehicleRequest.getModel();
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
