package com.vrrom.vehicle.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleDTO {
    @NotBlank(message = "Brand must not be blank")
    private String brand;

    @NotBlank(message = "Model must not be blank")
    private String model;

    @Min(value = 1885, message = "Year must be greater than the year the first car was made")
    @PastOrPresent(message = "Year must be in the past or the current year")
    private int year;

    @NotNull(message = "Fuel type must not be null")
    private FuelType fuel;

    @NotNull(message = "Emission status must not be null")
    private Emission emission;
}
