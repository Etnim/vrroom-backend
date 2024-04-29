package com.vrrom.vehicle.mapper;

import com.vrrom.application.model.Application;
import com.vrrom.vehicle.dtos.VehicleRequest;
import com.vrrom.vehicle.dtos.VehicleResponse;
import com.vrrom.vehicle.model.VehicleDetails;

public class VehicleMapper {
    public static void toEntity(VehicleDetails vehicle, VehicleRequest vehicleRequest, Application application) {
        vehicle.setBrand(vehicleRequest.getBrand());
        vehicle.setModel(vehicleRequest.getModel());
        vehicle.setFuel(vehicleRequest.getFuel());
        vehicle.setYear(vehicleRequest.getYear());
        vehicle.setEmissionStart(vehicleRequest.getEmissionStart());
        vehicle.setEmissionEnd(vehicleRequest.getEmissionEnd());
        vehicle.setApplication(application);
    }

    public static VehicleResponse toResponse(VehicleDetails vehicle){
        VehicleResponse response = new VehicleResponse();
        response.setMake(vehicle.getBrand());
        response.setModel(vehicle.getModel());
        response.setYear(vehicle.getYear());
        response.setFuel(vehicle.getFuel().getFuelTypeText());
        response.setEmissionStart(vehicle.getEmissionStart());
        response.setEmissionEnd(vehicle.getEmissionEnd());

        return response;
    }
}
