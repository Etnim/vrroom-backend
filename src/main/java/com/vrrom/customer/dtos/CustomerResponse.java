package com.vrrom.customer.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private long personalId;
    private String fullName;
    private int age;
    private String email;
    private String phone;
    private char creditRating;
}
