package com.vrrom.email.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {
    @Value("${mailgun.api.key}")
    private String apiKey;
    @Value("${mailgun.domain}")
    private String domain;
    private final RestTemplate restTemplate;

    public EmailService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sendEmail(String from, String to, String subject, String text) {
        String url = "https://api.mailgun.net/v3/" + domain + "/messages";
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("api", apiKey);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String body = String.format("from=%s&to=%s&subject=%s&text=%s",
                from, to, subject, text);
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response.getBody();
    }
}