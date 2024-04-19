package com.vrrom.customer;

import com.vrrom.application.model.Application;
import com.vrrom.customer.dtos.CustomerRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @Column(name = "pid")
    private long pid;

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

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Application application;

    public static class Builder {
        private long pid;
        private String name;
        private String surname;
        private String email;
        private LocalDate birthDate;
        private String phone;
        private String address;
        private Application application;

        public Builder withCustomerDTO(CustomerRequest customerRequest) {
            this.pid = customerRequest.getPid();
            this.name = customerRequest.getName();
            this.surname = customerRequest.getSurname();
            this.email = customerRequest.getEmail();
            this.birthDate = customerRequest.getBirthDate();
            this.phone = customerRequest.getPhone();
            this.address = customerRequest.getAddress();
            return this;
        }

        public Builder withApplication(Application application) {
            this.application = application;
            return this;
        }

        public Customer build() {
            Customer customer = new Customer();
            customer.setPid(this.pid);
            customer.setName(this.name);
            customer.setSurname(this.surname);
            customer.setEmail(this.email);
            customer.setBirthDate(this.birthDate);
            customer.setPhone(this.phone);
            customer.setAddress(this.address);
            customer.setApplication(this.application);
            return customer;
        }
    }

    public void assignRandomCreditRating() {
        double randomValue = new Random().nextDouble();
        if (randomValue <= 0.2) {
            this.creditRating = 1; // Rating A
        } else if (randomValue <= 0.6) {
            this.creditRating = 2; // Rating B
        } else if (randomValue <= 0.8) {
            this.creditRating = 3; // Rating C
        } else if (randomValue <= 0.95) {
            this.creditRating = 4; // Rating D
        } else {
            this.creditRating = 5;
        }
    }
}