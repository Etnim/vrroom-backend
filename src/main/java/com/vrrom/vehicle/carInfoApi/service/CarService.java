package com.vrrom.vehicle.carInfoApi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vrrom.email.service.EmailService;
import com.vrrom.util.SanitizationUtils;
import com.vrrom.vehicle.carInfoApi.exception.CarAPIException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {
    private final RestTemplate restTemplate;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    final Logger logger = LogManager.getLogger(CarService.class);

    public CarService(RestTemplate restTemplate, EmailService emailService, ObjectMapper objectMapper) {
        this.emailService = emailService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Cacheable(cacheNames = "makesCache", unless = "#result == null || #result.isEmpty()")
    public List<String> getMakes() throws CarAPIException {
        URI url = UriComponentsBuilder
                .fromHttpUrl("https://vpic.nhtsa.dot.gov/api/vehicles")
                .pathSegment("GetMakesForVehicleType")
                .pathSegment("car")
                .queryParam("format", "json")
                .build()
                .encode()
                .toUri();
        return getCarInfo(url, "MakeName");
    }

    @Cacheable(cacheNames = "modelsCache", key = "#make")
    public List<String> getModels(String make) throws CarAPIException {
        String sanitizedMake = SanitizationUtils.sanitizeCarMake(make);
        URI url = UriComponentsBuilder
                .fromHttpUrl("https://vpic.nhtsa.dot.gov/api/vehicles")
                .pathSegment("GetModelsForMake")
                .pathSegment(sanitizedMake)
                .queryParam("format", "json")
                .build()
                .encode()
                .toUri();
        return getCarInfo(url, "Model_Name");
    }

    private List<String> getCarInfo(URI url, String fieldName) throws CarAPIException {
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new CarAPIException(new Throwable("API is currently down."), response.getStatusCode());
            }
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode results = rootNode.path("Results");
            List<String> carInfo = new ArrayList<>();
            if (results.isArray()) {
                for (JsonNode resultNode : results) {
                    carInfo.add(resultNode.path(fieldName).asText());
                }
            }
            logger.info("Successfully retrieved car info from API.");
            return carInfo;
        } catch (Exception e) {
            handleApiDown(url.toString(), e.getMessage());
            logger.error("Failed to process the API data", e);
            throw new CarAPIException("Failed to process the API data: " + e.getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void handleApiDown(String externalUrl, String errorMessage) {
        String from = "vrroom.leasing@gmail.com";
        String to = "vrroom.leasing@gmail.com";
        String subject = "API Down Alert";
        String text = externalUrl + errorMessage;
        emailService.sendEmail(from, to, subject, text);
    }
}




