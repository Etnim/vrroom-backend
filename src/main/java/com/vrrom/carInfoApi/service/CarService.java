package com.vrrom.carInfoApi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vrrom.exception.VehicleServiceException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class CarService {
    private final RestTemplate restTemplate;

    public CarService() {
        this.restTemplate = new RestTemplate();
    }

    @Cacheable(cacheNames = "makesCache")
    public String getMakes() {
        URI url = UriComponentsBuilder
                .fromHttpUrl("https://vpic.nhtsa.dot.gov/api/vehicles")
                .pathSegment("GetMakesForVehicleType")
                .pathSegment("car")
                .queryParam("format", "json")
                .build()
                .encode()
                .toUri();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
            String responseBody = response.getBody();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode;
            try {
                rootNode = mapper.readTree(responseBody);
            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format in response.", e);
            }
            JsonNode resultsNode = rootNode.path("Results");
            if (resultsNode.isArray() && resultsNode.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No results found for car makes.");
            }
            return responseBody;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to fetch data from the API.");
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
}




