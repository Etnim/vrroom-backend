package com.vrrom.customer.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private long personalId;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
    private char creditRating;
    private String address;
}
