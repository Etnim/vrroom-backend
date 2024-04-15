package com.vrrom.financialInfo.controller;

import com.vrrom.financialInfo.service.FinancialInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/finances/{id}")
@Tag(name = "Financial information Controller", description = "To work with financial data of the customer")
public class FinancialInfoController {
    private final FinancialInfoService financialInfoService;

    @Autowired
    public FinancialInfoController(FinancialInfoService financialInfoService) {
        this.financialInfoService = financialInfoService;
    }
}
