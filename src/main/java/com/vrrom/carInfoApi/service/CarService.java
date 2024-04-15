package com.vrrom.carInfoApi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vrrom.exception.VehicleServiceException;
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

    public String getModels(String make, int year) throws VehicleServiceException {
        URI url = UriComponentsBuilder
                .fromHttpUrl("https://vpic.nhtsa.dot.gov/api/vehicles")
                .pathSegment("GetModelsForMakeYear")
                .pathSegment("make", make)
                .pathSegment("modelyear", Integer.toString(year))
                .queryParam("format", "json")
                .build()
                .encode()
                .toUri();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            // Throws custom exception with detailed error information
            throw new VehicleServiceException("HTTP error occurred with status " + e.getStatusCode() + ": " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            // General error handling for REST client issues
            throw new VehicleServiceException("Error during REST call to the API: " + e.getMessage(), e);
        } catch (Exception e) {
            // Fallback for unexpected errors
            throw new VehicleServiceException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }
}
//slow approach, but mainstream brands- i do not like i
//    private static final String BASE_URL = "https://www.carqueryapi.com/api/0.3/";
//    public String getMakes(int year) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
//                .queryParam("cmd", "getMakes")
//                .queryParam("year", year)
//                .toUriString();
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//        return response.getBody();
//    }
//    public Set<String> getMakesFromRange(int startYear, int endYear) {
//        Set<String> allMakes = new HashSet<>();
//        ObjectMapper mapper = new ObjectMapper();
//        for (int year = startYear; year <= endYear; year++) {
//            String json = getMakes(year);
//            try {
//                JsonNode rootNode = mapper.readTree(json);
//                JsonNode makesArray = rootNode.path("Makes");
//                if (makesArray.isArray()) {
//                    for (JsonNode makeNode : makesArray) {
//                        String makeDisplay = makeNode.path("make_display").asText();
//                        allMakes.add(makeDisplay);
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                // Handle the exception properly in real scenarios
//            }
//        }
//        return allMakes;
//    }



