package com.vrrom.euribor.service;

import com.vrrom.email.service.NotificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
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
    private final NotificationService notificationService;
    private static final Logger log = LogManager.getLogger(EuriborService.class);

    public EuriborService(WebClient webClient, NotificationService notificationService) {
        this.webClient = webClient;
        this.notificationService = notificationService;
    }

    @Cacheable(value = "euriborRates", key = "#term")
    public Mono<String> fetchEuriborRates(String term) {
        return webClient.get()
                .uri(baseUrl + "/" + term)
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", apiHost)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new RuntimeException("API error: " + errorBody))))
                .bodyToMono(String.class)
                .onErrorResume(e -> {
                    log.error("Error retrieving rates for term {}: {}", term, e.getMessage());
                    notificationService.notify("API Error", "Detected an API failure for term " + term + ": " + e.getMessage(), "vrroom.leasing@gmail.com");
                    return Mono.just("API is currently down. Please try again later.");
                })
                .doOnSuccess(body -> log.info("Successfully retrieved EURIBOR rates for term {}: {}", term, body));
    }
}



