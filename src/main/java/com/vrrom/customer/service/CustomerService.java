package com.vrrom.customer.service;

import com.vrrom.application.exception.ApplicationException;
import com.vrrom.application.model.Application;
import com.vrrom.customer.Customer;
import com.vrrom.customer.exception.CustomerException;
import com.vrrom.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer findCustomerById(long id) {
        return customerRepository.findById(id)
                .orElse(null);
    }
}
