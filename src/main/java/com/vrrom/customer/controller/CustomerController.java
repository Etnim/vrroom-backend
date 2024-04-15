package com.vrrom.customer.controller;

import com.vrrom.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/customers")
@Tag(name = "customer Controller", description = "To work with customer personal data")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
}
