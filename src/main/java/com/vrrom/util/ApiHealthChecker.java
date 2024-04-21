package com.vrrom.util;

import com.vrrom.vehicle.carInfoApi.exception.CarAPIException;
import com.vrrom.vehicle.carInfoApi.service.CarService;
import com.vrrom.email.service.EmailService;
import com.vrrom.euribor.service.EuriborService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ApiHealthChecker {
    private final CarService carService;
    private final EuriborService euriborService;

    @Autowired
    public ApiHealthChecker(CarService carService, EuriborService euriborService) {
        this.carService = carService;
        this.euriborService = euriborService;
    }

    @Scheduled(fixedRate = 43200000) // 12 hours
    public void checkApiHealth() throws CarAPIException {
        checkCarMakesHealth();
        checkCarModelsForMakeHealth("Toyota");
        checkEuriborRatesHealth("3m");
        checkEuriborRatesHealth("6m");
    }

    private void checkCarMakesHealth() throws CarAPIException {
        carService.getMakes();
    }

    private void checkCarModelsForMakeHealth(String make) throws CarAPIException {
        carService.getModels(make);
    }

    private void checkEuriborRatesHealth(String term) {
        euriborService.fetchEuriborRates(term).subscribe();
    }
}
