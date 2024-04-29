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
    private String make;
    private String model;
    private int year;
    private String fuel;
    private int emissionStart;
    private int emissionEnd;
}
