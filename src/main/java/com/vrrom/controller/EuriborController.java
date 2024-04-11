package com.vrrom.controller;

import com.vrrom.service.EuriborService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/euribor")
public class EuriborController {

    private final EuriborService euriborService;

    public EuriborController(EuriborService euriborService) {
        this.euriborService = euriborService;
    }

    @GetMapping
    public String getEuriborRates() {
        return euriborService.fetchEuriborRates();
    }
}
