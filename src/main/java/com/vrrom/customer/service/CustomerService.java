package com.vrrom.customer.service;

import com.vrrom.customer.model.Customer;
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
