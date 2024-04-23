package com.vrrom.vehicle.dtos;

import com.vrrom.vehicle.model.FuelType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponse {
    @NotBlank(message = "Brand must not be blank")
    private String make;

    @NotBlank(message = "Model must not be blank")
    private String model;

    @Min(value = 1885, message = "Year must be greater than the year the first car was made")
    private int year;

    @NotNull(message = "Fuel type must not be null")
    private FuelType fuel;

    @Min(value = 0, message = "Emission value can not be negative")
    private int emissionStart;

    @Max(value = 130, message = "Emission value can not be more than 130")
    private int emissionEnd;
}
