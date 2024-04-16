package com.vrrom.carInfoApi.controller;

import com.vrrom.exception.VehicleServiceException;
import com.vrrom.carInfoApi.service.CarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.vrrom.util.SanitizationUtils.sanitizeCarMake;

@RestController
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/makes")
    public String getCarMakes() {

            return carService.getMakes();

    }

    @GetMapping("/models/{make}")
    public String getCarModels(@PathVariable String make) {
        make = sanitizeCarMake(make);
        try {
            return carService.getModels(make);
        } catch (VehicleServiceException e) {
            throw new RuntimeException(e);
        }
    }
}