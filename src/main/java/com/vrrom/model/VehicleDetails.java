package com.vrrom.model;

import java.util.Date;
import java.util.Objects;

public class VehicleDetails {
    private Vehicle Vehicle;
    private String make;
    private String model;
    private Date year;
    private Fuel fuel;
    private int emission;

    public VehicleDetails(com.vrrom.model.Vehicle vehicle, String make, String model, Date year, Fuel fuel, int emission) {
        Vehicle = vehicle;
        this.make = make;
        this.model = model;
        this.year = year;
        this.fuel = fuel;
        this.emission = emission;
    }

    @Override
    public String toString() {
        return "VehicleDetails{" +
                "Vehicle=" + Vehicle +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", fuel=" + fuel +
                ", emission=" + emission +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VehicleDetails that)) return false;
        return emission == that.emission && Vehicle == that.Vehicle && Objects.equals(make, that.make) && Objects.equals(model, that.model) && Objects.equals(year, that.year) && fuel == that.fuel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Vehicle, make, model, year, fuel, emission);
    }

    public com.vrrom.model.Vehicle getVehicle() {
        return Vehicle;
    }

    public void setVehicle(com.vrrom.model.Vehicle vehicle) {
        Vehicle = vehicle;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getYear() {
        return year;
    }

    public void setYear(Date year) {
        this.year = year;
    }

    public Fuel getFuel() {
        return fuel;
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel;
    }

    public int getEmission() {
        return emission;
    }

    public void setEmission(int emission) {
        this.emission = emission;
    }
}
