package com.vrrom.vehicle.carInfoApi.service;

import com.vrrom.email.service.EmailService;
import com.vrrom.util.exception.VehicleServiceException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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

    public CarService(RestTemplate restTemplate, EmailService emailService) {
        this.emailService = emailService;
        this.restTemplate = restTemplate;
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
            handleApiDown("The API endpoint for vehicle makes is down. ", e.getResponseBodyAsString());
            throw new ResponseStatusException(e.getStatusCode(), "API failure: " + e.getMessage(), e);
        } catch (Exception e) {
            handleApiDown("The API endpoint for vehicle makes is down. ", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "API failure: " + e.getMessage(), e);
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
            handleApiDown("The API endpoint for vehicle models is down. Error message: ", "HTTP error occurred with status " + e.getStatusCode() + ": " + e.getResponseBodyAsString());
            throw new VehicleServiceException("HTTP error occurred with status " + e.getStatusCode() + ": " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            handleApiDown("The API endpoint for vehicle models is down. Error message: ", "Error during REST call to the API: " + e.getMessage());
            throw new VehicleServiceException("Error during REST call to the API: " + e.getMessage(), e);
        } catch (Exception e) {
            handleApiDown("The API endpoint for vehicle models is down. Error message:", "An unexpected error occurred: " + e.getMessage());
            throw new VehicleServiceException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }

    private void handleApiDown(String endpointMessage, String errorMessage) {
        String from = "vrroom.leasing@gmail.com";
        String to = "vrroom.leasing@gmail.com";
        String subject = "API Down Alert";
        String text = endpointMessage + errorMessage;
        emailService.sendEmail(from, to, subject, text);
    }
}




