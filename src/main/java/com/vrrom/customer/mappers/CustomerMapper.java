package com.vrrom.customer.mappers;

import com.vrrom.application.model.Application;
import com.vrrom.customer.Customer;
import com.vrrom.customer.dtos.CustomerDTO;

public class CustomerMapper {
    public static Customer toEntity(CustomerDTO customerDTO, Application application) {
        return new Customer.Builder()
                .withCustomerDTO(customerDTO)
                .withApplication(application)
                .build();
    }
}
