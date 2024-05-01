package com.vrrom.customer.controller;

import com.vrrom.customer.Customer;
import com.vrrom.customer.dtos.CustomerResponse;
import com.vrrom.customer.mappers.CustomerMapper;
import com.vrrom.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get customer")
    public CustomerResponse getCustomerById(@PathVariable long id) {
        Customer customer = customerService.findCustomerById(id);
        return CustomerMapper.toResponse(customer);
    }
}
