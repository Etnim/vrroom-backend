package com.vrrom.controller;

import com.vrrom.service.EuriborService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/euribor")
public class EuriborController {
    private static final Logger logger = LoggerFactory.getLogger(EuriborController.class);
    private final EuriborService euriborService;

    public EuriborController(EuriborService euriborService) {
        this.euriborService = euriborService;
    }

    @GetMapping("/{term}")
    public Mono<ResponseEntity<String>> getEuriborRates(@PathVariable String term) {
        logger.info("Received request for Euribor rates with term: {}", term);
        if (!term.matches("3m|6m")) {
            logger.error("Invalid term provided: {}", term);
            throw new IllegalArgumentException("Invalid term. Use '3m' or '6m' only.");
        }
        return euriborService.fetchEuriborRates(term)
                .map(data -> ResponseEntity.ok().body(data))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
