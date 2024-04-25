package com.vrrom.vehicle.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FuelType {
    PETROL("Petrol"),
    DIESEL("Diesel"),
    ELECTRIC("Electric"),
    HYBRID("Hybrid");

    private final String fuelTypeText;

    FuelType(String fuelTypeText) {
        this.fuelTypeText = fuelTypeText;
    }

    @JsonValue
    public String getFuelTypeText() {
        return fuelTypeText;
    }
}
