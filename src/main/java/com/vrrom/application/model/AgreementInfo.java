package com.vrrom.application.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
public class AgreementInfo {
 private Long pid;
 private String customerName;
 private String customerSurname;
 private LocalDate customerBirthDate;
 private String customerAddress;
 private String customerEmail;
 private String customerPhone;
 private LocalDate agreementDate;
 private String carMake;
 private String carModel;
 private int carYear;
 private BigDecimal leasingAmount;
 private int leasingYearPeriod;
 private BigDecimal monthlyPayment;
 private LocalDate firstPaymentDate;
 private double interestRate;
}
