package com.vrrom.car;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vrrom.vehicle.carInfoApi.service.CarService;
import com.vrrom.email.service.EmailService;
import com.vrrom.util.exception.VehicleServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
class CarServiceTest {
    @Mock
    private RestTemplate restTemplate;
    private CarService carService;
    @Mock
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        carService = new CarService(restTemplate, emailService);
    }

    @Test
    public void testGetMakes_CheckKeyFields() throws Exception {
        String jsonResponse = "{\"Count\": 10, \"Message\": \"Response returned successfully\", \"SearchCriteria\": \"Vehicle Type: car\", \"Results\": [{\"MakeName\": \"Test Car\"}]}";
        URI uri = URI.create("https://vpic.nhtsa.dot.gov/api/vehicles/GetMakesForVehicleType/car?format=json");
        when(restTemplate.exchange(eq(uri), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(jsonResponse, HttpStatus.OK));
        String result = carService.getMakes();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(result);
        assertTrue("Should have 'Count' field", rootNode.has("Count"));
        assertTrue("Should have 'Message' field", rootNode.has("Message"));
        assertTrue("Should have 'Results' field", rootNode.has("Results"));
    }

    @Test
    public void testGetMakes_HandleHttpStatusError() {
        URI uri = URI.create("https://vpic.nhtsa.dot.gov/api/vehicles/GetMakesForVehicleType/car?format=json");
        when(restTemplate.exchange(eq(uri), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        try {
            carService.getMakes();
        } catch (ResponseStatusException e) {
            assertTrue("Should handle HTTP 500 Internal Server Error", e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Test
    void getModels_Success_CheckKeyFields() throws Exception {
        String make = "Toyota";
        String jsonResponse = "{\n" +
                "  \"Count\": 56,\n" +
                "  \"Message\": \"Response returned successfully\",\n" +
                "  \"SearchCriteria\": \"Make:toyota\",\n" +
                "  \"Results\": [\n" +
                "    {\"Make_ID\": 448, \"Make_Name\": \"Toyota\", \"Model_ID\": 2206, \"Model_Name\": \"Scion xA\"}\n" +
                "  ]\n" +
                "}";
        URI uri = URI.create("https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMake/Toyota?format=json");
        when(restTemplate.getForEntity(uri, String.class))
                .thenReturn(new ResponseEntity<>(jsonResponse, HttpStatus.OK));
        String response = carService.getModels(make);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response);
        assertTrue("Should have 'Count' field", rootNode.has("Count"));
        assertTrue("Should have 'Message' field", rootNode.has("Message"));
        assertTrue("Should have 'SearchCriteria' field", rootNode.has("SearchCriteria"));
        assertTrue("Should have 'Results' field", rootNode.has("Results"));
        assertTrue("Results should not be empty", rootNode.path("Results").isArray() && rootNode.path("Results").size() > 0);
        JsonNode firstResult = rootNode.path("Results").get(0);
        assertTrue("Should have 'Make_ID' field", firstResult.has("Make_ID"));
        assertTrue("Should have 'Make_Name' field", firstResult.has("Make_Name"));
        assertTrue("Should have 'Model_ID' field", firstResult.has("Model_ID"));
        assertTrue("Should have 'Model_Name' field", firstResult.has("Model_Name"));
    }

    @Test
    public void testGetMakes_HandleHttpStatusErrorAndSendEmail() {
        URI uri = URI.create("https://vpic.nhtsa.dot.gov/api/vehicles/GetMakesForVehicleType/car?format=json");
        when(restTemplate.exchange(eq(uri), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        try {
            carService.getMakes();
            fail("Expected an ResponseStatusException to be thrown");
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
        }
        verify(emailService).sendEmail(
                eq("vrroom.leasing@gmail.com"),
                eq("vrroom.leasing@gmail.com"),
                eq("API Down Alert"),
                contains("The API endpoint for vehicle makes is down."));
    }

    @Test
    public void testGetModelsApiFailureSendsEmail() throws Exception {
        String make = "Toyota";
        URI uri = new URI("https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMake/" + make + "?format=json");
        when(restTemplate.getForEntity(uri, String.class)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        try {
            carService.getModels(make);
            fail("Expected a VehicleServiceException to be thrown");
        } catch (VehicleServiceException e) {
            assertNotNull(e.getMessage());
        }
        verify(emailService).sendEmail(
                eq("vrroom.leasing@gmail.com"),
                eq("vrroom.leasing@gmail.com"),
                eq("API Down Alert"),
                contains("The API endpoint for vehicle models is down." + " Error message: " + "HTTP error occurred with status " + HttpStatus.BAD_REQUEST + ": "));
    }
}
