package com.vrrom.customer;

import com.vrrom.application.model.Application;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pid;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birthDate")
    private Date birthDate;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "credit_rating")
    private int creditRating;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Application application;

    public Customer(String name, String surname, Date birthDate, String email, String phone, String address) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.address = address;
        assignRandomCreditRating();
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
            this.creditRating = 5; // Rating E
        }
    }
}