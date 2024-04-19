package com.vrrom.customer.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    @NotBlank(message = "Name is mandatory")
    private long pid;

    @NotBlank(message = "Full name is mandatory")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Full name should contain only letters")
    @Size(max = 70, message = "Full name must not be longer than 70 characters")
    private String fullName;

    @Min(value = 18,message = "Age should bot be less that 18")
    private int age;

    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "^\\+370[0-9]{9}$", message = "Phone should contain only numbers")
    private String phone;
}
