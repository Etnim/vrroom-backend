package com.vrrom.customer.mappers;

import com.vrrom.application.model.Application;
import com.vrrom.customer.Customer;
import com.vrrom.customer.dtos.CustomerRequest;
import com.vrrom.customer.dtos.CustomerResponse;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CustomerMapper {
    public static Customer toEntity(CustomerRequest customerRequest, Application application) {
        return new Customer.Builder()
                .withCustomerDTO(customerRequest)
                .withApplication(application)
                .build();
    }

    public static CustomerResponse toResponse(Customer customer){
        String fullName = customer.getName()+customer.getSurname();
        LocalDate birthDate = customer.getBirthDate();
        int age = (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());

        CustomerResponse response = new CustomerResponse();
        response.setPid(customer.getPersonalId());
        response.setFullName(fullName);
        response.setAge(age);
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        return response;
    }
}
