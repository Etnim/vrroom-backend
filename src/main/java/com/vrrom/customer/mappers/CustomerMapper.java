package com.vrrom.customer.mappers;

import com.vrrom.application.model.Application;
import com.vrrom.customer.dtos.CustomerResponse;
import com.vrrom.customer.model.Customer;
import com.vrrom.customer.dtos.CustomerRequest;
import com.vrrom.customer.dtos.CustomerResponseForAdmin;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CustomerMapper {
    public static Customer toEntity(CustomerRequest customerRequest, Application application) {
        return new Customer.Builder()
                .withCustomerDTO(customerRequest)
                .withApplication(application)
                .build();
    }

    public static CustomerResponseForAdmin toAdminResponse(Customer customer) {
        String fullName = customer.getName() + " " + customer.getSurname();
        LocalDate birthDate = customer.getBirthDate();
        int age = (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
        CustomerResponseForAdmin response = new CustomerResponseForAdmin();
        response.setPersonalId(customer.getPersonalId());
        response.setFullName(fullName);
        response.setAge(age);
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setCreditRating((char)(customer.getCreditRating() + 64));
        return response;
    }
    public static CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setPersonalId(customer.getPersonalId());
        response.setName(customer.getName());
        response.setSurname(customer.getSurname());
        response.setDateOfBirth(customer.getBirthDate());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setAddress(customer.getAddress());
        response.setCreditRating((char)(customer.getCreditRating() + 64));
        return response;
    }
}
