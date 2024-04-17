package com.vrrom.carInfoApi.controller;

import com.vrrom.carInfoApi.service.CarService;
import com.vrrom.exception.VehicleServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/makes")
    public String getCarMakes() {
        return carService.getMakes();
    }

    @GetMapping("/models/{make}")
    public ResponseEntity<String> getCarModels(@PathVariable String make) {
        try {
            String result = carService.getModels(make);
            return ResponseEntity.ok(result);
        } catch (VehicleServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}