package com.vrrom.customer.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
public class CustomerRequest {

    @NotBlank(message = "Name is mandatory")
    private long pid;

    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name should contain only letters")
    @Size(max = 25, message = "Name must not be longer than 25 characters")
    private String name;

    @NotBlank(message = "Surname is mandatory")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name should contain only letters")
    @Size(max = 25, message = "Name must not be longer than 25 characters")
    private String surname;

    @Email(message = "Email should be valid")
    private String email;

    @Past(message = "Birthdate should be in the past")
    private LocalDate birthDate;

    @Pattern(regexp = "^\\+370[0-9]{9}$", message = "Phone should contain only numbers")
    private String phone;

    @NotBlank(message = "Address is mandatory")
    private String address;
}
