package com.vrrom.vehicle.repository;

import com.vrrom.vehicle.model.VehicleDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<VehicleDetails, Long> {
}
