package com.vrrom.customer.dtos;

import com.vrrom.customer.validator.minAge.MinimumAge;
import com.vrrom.customer.validator.personalId.PersonalId;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
public class CustomerRequest {

    @PersonalId(value = 11, message = "personal Id should consist exactly 11 digits")
    private long personalId;

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
    @MinimumAge(value = 18, message = "Customer must be at least 18 years old")
    private LocalDate birthDate;

    @Pattern(regexp = "^\\+370[0-9]{8}$", message = "Phone should be valid")
    private String phone;

    @NotBlank(message = "Address is mandatory")
    private String address;
}
