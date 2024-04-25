package com.vrrom.agreement.dto;

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
 private String carMake;
 private String carModel;
 private int carYear;
 private BigDecimal leasingAmount;
 private BigDecimal downPayment;
 private int leasingYearPeriod;
 private BigDecimal residualValue;
 private BigDecimal agreementFee;
}
