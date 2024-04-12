package com.vrrom.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

