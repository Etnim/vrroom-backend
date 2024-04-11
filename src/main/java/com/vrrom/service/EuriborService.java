package com.vrrom.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Service
public class EuriborService {
    @Value("${api-ninjas.euribor.base-url}")
    private String baseUrl;

    @Value("${api-ninjas.euribor.api-key}")
    private String apiKey;
    private final RestTemplate restTemplate;

    public EuriborService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchEuriborRates() {

        String url = baseUrl;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("name", "tonar");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            return response.getBody();
        } catch (HttpClientErrorException ex) {
            return "Error fetching Euribor rates: " + ex.getStatusCode();
        }
    }


}

