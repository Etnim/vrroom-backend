package com.vrrom.euribor.service;

import com.vrrom.email.service.EmailService;
import com.vrrom.euribor.dto.EuriborRate;
import com.vrrom.euribor.exception.EuriborAPIException;
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
    private String BASE_URL;
    @Value("${api.euribor.key}")
    private String API_KEY;
    @Value("${api.euribor.host}")
    private String API_HOST;
    private final EmailService emailService;
    private static final Logger log = LogManager.getLogger(EuriborService.class);

    public EuriborService(WebClient webClient, EmailService emailService) {
        this.webClient = webClient;
        this.emailService = emailService;
    }

    @Cacheable(value = "euriborRates", key = "#term")
    public Mono<Double> fetchEuriborRates(String term) {
        return webClient.get()
                .uri(BASE_URL + "/" + term)
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(EuriborRate.class)
                        .flatMap(errorBody -> Mono.error(new EuriborAPIException("Euribor APi is down: ", response.statusCode()))))
                .bodyToMono(EuriborRate.class)
                .map(EuriborRate::getRate)
                .onErrorResume(e -> {
                    log.error("Error retrieving rates for term {}: {}", term, e.getCause().getMessage());
                    handleApiDown("Failed to retrieve EURIBOR rates for term " + term + ": ", e.getCause().getMessage());
                    return Mono.error(new EuriborAPIException("Unable to process request: ", HttpStatusCode.valueOf(502)));
                })
                .doOnSuccess(body -> log.info("Successfully retrieved EURIBOR rates for term {}: {}", term, body));
    }

    private void handleApiDown(String endpointMessage, String errorMessage) {
        String from = "vrroom.leasing@gmail.com";
        String to = "vrroom.leasing@gmail.com";
        String subject = "API Down Alert";
        String text = endpointMessage + errorMessage;
        emailService.sendEmail(from, to, subject, text);
    }
}



