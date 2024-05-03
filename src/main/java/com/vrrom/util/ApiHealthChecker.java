package com.vrrom.util;

import com.vrrom.email.exception.EmailServiceException;
import com.vrrom.euribor.service.EuriborService;
import com.vrrom.vehicle.carInfoApi.exception.CarAPIException;
import com.vrrom.vehicle.carInfoApi.service.CarService;
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
    public void checkApiHealth() throws CarAPIException, EmailServiceException {
        checkCarMakesHealth();
        checkCarModelsForMakeHealth("Toyota");
        checkEuriborRatesHealth("3m");
        checkEuriborRatesHealth("6m");
    }

    private void checkCarMakesHealth() throws CarAPIException, EmailServiceException {
        carService.getMakes();
    }

    private void checkCarModelsForMakeHealth(String make) throws CarAPIException, EmailServiceException {
        carService.getModels(make);
    }

    private void checkEuriborRatesHealth(String term) {
        euriborService.fetchEuriborRates(term).subscribe();
    }
}
