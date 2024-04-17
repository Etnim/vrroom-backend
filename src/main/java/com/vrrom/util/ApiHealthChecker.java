package com.vrrom.util;

import com.vrrom.carInfoApi.service.CarService;
import com.vrrom.email.service.EmailService;
import com.vrrom.euribor.service.EuriborService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ApiHealthChecker {
    private final CarService carService;
    private final EmailService emailService;
    private final EuriborService euriborService;

    @Autowired
    public ApiHealthChecker(CarService carService, EmailService emailService, EuriborService euriborService) {
        this.carService = carService;
        this.emailService = emailService;
        this.euriborService = euriborService;
    }

    @Scheduled(fixedRate = 43200000) // 12 hours
    public void checkApiHealth() {
        checkCarMakesHealth();
        checkCarModelsForMakeHealth("Toyota");
        checkEuriborRatesHealth("3m");
        checkEuriborRatesHealth("6m");
    }

    private void checkCarMakesHealth() {
        try {
            carService.getMakes();
        } catch (Exception e) {
            notifyDowntime("Health check failed for vehicle models API: " + e.getMessage());
        }
    }

    private void checkCarModelsForMakeHealth(String make) {
        try {
            carService.getModels(make);
        } catch (Exception e) {
            notifyDowntime("Health check failed for vehicle models API: " + e.getMessage());
        }
    }

    private void checkEuriborRatesHealth(String term) {
        euriborService.fetchEuriborRates(term).subscribe(
                success -> {
                },
                error -> notifyDowntime("Health check failed for Euribor rates API for term " + term + ": " + error.getMessage())
        );
    }

    private void notifyDowntime(String message) {
        emailService.sendEmail("vrroom.leasing@gmail.com", "vrroom.leasing@gmail.com", "API Downtime Alert", message);
    }
}
