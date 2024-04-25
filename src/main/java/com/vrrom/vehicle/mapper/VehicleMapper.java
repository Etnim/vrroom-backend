package com.vrrom.vehicle.mapper;

import com.vrrom.application.model.Application;
import com.vrrom.vehicle.dtos.VehicleRequest;
import com.vrrom.vehicle.dtos.VehicleResponse;
import com.vrrom.vehicle.model.VehicleDetails;

import java.util.List;
import java.util.stream.Collectors;

public class VehicleMapper {
    public static VehicleDetails toEntity(VehicleRequest vehicleRequest, Application application) {
        return new VehicleDetails.Builder()
                .withVehicleDTO(vehicleRequest)
                .withApplication(application)
                .build();
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
