package com.vrrom.carInfoApi.service;

import com.vrrom.email.service.EmailService;
import com.vrrom.exception.VehicleServiceException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class CarService {
    private final RestTemplate restTemplate;
    private final EmailService emailService;

    public CarService(EmailService emailService) {
        this.emailService = emailService;
        this.restTemplate = new RestTemplate();
    }




    @Cacheable(cacheNames = "makesCache", unless = "#result == null")
    public String getMakes() {
        URI url = UriComponentsBuilder
                .fromHttpUrl("https://vpic.nhtsa.dot.gov/api/vehicles")
                .pathSegment("GetMakesForVehicleType")
                .pathSegment("car")
                .queryParam("format", "json")
                .build()
                .encode()
                .toUri();
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ResponseStatusException(response.getStatusCode(), "API is currently down.");
            }
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            emailService.sendEmail("api-monitor@vrrom.com", "admin@vrrom.com",
                    "API Down Alert", "The API endpoint for vehicle makes is down. Error: " + e.getMessage());
            throw new ResponseStatusException(e.getStatusCode(), "API failure: " + e.getMessage(), e);
        }
    }


    @Cacheable(cacheNames = "modelsCache", key = "#make")
    public String getModels(String make) throws VehicleServiceException {
        URI url = UriComponentsBuilder
                .fromHttpUrl("https://vpic.nhtsa.dot.gov/api/vehicles")
                .pathSegment("GetModelsForMake")
                .pathSegment(make)
                .queryParam("format", "json")
                .build()
                .encode()
                .toUri();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new VehicleServiceException("HTTP error occurred with status " + e.getStatusCode() + ": " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            throw new VehicleServiceException("Error during REST call to the API: " + e.getMessage(), e);
        } catch (Exception e) {

            throw new VehicleServiceException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }

    private void handleApiDown(String errorMessage) {
        String from = "you@example.com";
        String to = "admin@example.com";
        String subject = "API Down Alert";
        String text = "The API is currently down. Error: " + errorMessage;
        emailService.sendEmail(from, to, subject, text);
    }
}




