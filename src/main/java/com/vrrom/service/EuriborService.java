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
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Service
public class EuriborService {
    private final WebClient webClient;

    @Value("${api.euribor.url}")
    private String baseUrl;

    @Value("${api.euribor.key}")
    private String apiKey;

    @Value("${api.euribor.host}")
    private String apiHost;

    public EuriborService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> fetchEuriborRates(String term) {
        return webClient.get()
                .uri(baseUrl + "/" + term)
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", apiHost)
                .retrieve()
                .bodyToMono(String.class);
    }

}

