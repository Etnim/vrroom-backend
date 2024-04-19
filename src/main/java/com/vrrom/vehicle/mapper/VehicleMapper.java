package com.vrrom.vehicle.mapper;

import com.vrrom.application.model.Application;
import com.vrrom.vehicle.model.VehicleDTO;
import com.vrrom.vehicle.model.VehicleDetails;

import java.util.List;
import java.util.stream.Collectors;

public class VehicleMapper {
    public static List<VehicleDetails> toEntityList(List<VehicleDTO> vehicleDTOS, Application application) {
        return vehicleDTOS.stream().map(dto -> toEntity(dto, application)).collect(Collectors.toList());
    }

    private static VehicleDetails toEntity(VehicleDTO vehicleDTO, Application application) {
        return new VehicleDetails.Builder()
                .withVehicleDTO(vehicleDTO)
                .withApplication(application)
                .build();
    }
}
