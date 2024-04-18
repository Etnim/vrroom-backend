package com.vrrom.vehicle.carInfoApi.controller;

import com.vrrom.vehicle.carInfoApi.service.CarService;
import com.vrrom.vehicle.carInfoApi.exception.CarAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/makes")
    public List<String> getCarMakes() throws CarAPIException {
        return carService.getMakes();
    }

    @GetMapping("/{make}/models")
    public List<String> getCarModels(@PathVariable String make) throws CarAPIException {
        return carService.getModels(make);
    }
}