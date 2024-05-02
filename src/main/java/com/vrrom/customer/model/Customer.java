package com.vrrom.customer.model;

import com.vrrom.application.model.Application;
import com.vrrom.customer.dtos.CustomerRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @Column(name = "personalId", unique = true )
    private long personalId;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birthDate")
    private LocalDate birthDate;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "credit_rating")
    private int creditRating;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Application> applications;

    public static class Builder {
        private long personalId;
        private String name;
        private String surname;
        private String email;
        private LocalDate birthDate;
        private String phone;
        private String address;
        private List<Application> applications = new ArrayList<>();

        public Builder withCustomerDTO(CustomerRequest customerRequest) {
            this.personalId = customerRequest.getPersonalId();
            this.name = customerRequest.getName();
            this.surname = customerRequest.getSurname();
            this.email = customerRequest.getEmail();
            this.birthDate = customerRequest.getBirthDate();
            this.phone = customerRequest.getPhone();
            this.address = customerRequest.getAddress();
            return this;
        }

        public Builder withApplication(Application application) {
            this.applications.add(application);
            return this;
        }

        public Customer build() {
            Customer customer = new Customer();
            customer.assignRandomCreditRating();
            customer.setPersonalId(this.personalId);
            customer.setName(this.name);
            customer.setSurname(this.surname);
            customer.setEmail(this.email);
            customer.setBirthDate(this.birthDate);
            customer.setPhone(this.phone);
            customer.setAddress(this.address);
            customer.setApplications(this.applications);
            return customer;
        }
    }

    public void assignRandomCreditRating() {
        double randomValue = new Random().nextDouble();
        if (randomValue <= 0.2) {
            this.creditRating = 1;
        } else if (randomValue <= 0.6) {
            this.creditRating = 2;
        } else if (randomValue <= 0.8) {
            this.creditRating = 3;
        } else if (randomValue <= 0.95) {
            this.creditRating = 4;
        } else {
            this.creditRating = 5;
        }
    }
}