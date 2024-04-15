package com.vrrom.euribor.controller;

import com.vrrom.euribor.service.EuriborService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/euribor")
public class EuriborController {
    private final EuriborService euriborService;

    public EuriborController(EuriborService euriborService) {
        this.euriborService = euriborService;
    }

    @GetMapping("/{term}")
    public Mono<ResponseEntity<String>> getEuriborRates(@PathVariable String term) {
        if (!term.matches("3m|6m")) {
            throw new IllegalArgumentException("Invalid term. Use '3m' or '6m' only.");
        }
        return euriborService.fetchEuriborRates(term)
                .map(data -> ResponseEntity.ok().body(data))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
