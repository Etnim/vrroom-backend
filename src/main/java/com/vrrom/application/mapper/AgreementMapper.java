package com.vrrom.application.mapper;

import com.vrrom.application.model.AgreementInfo;
import com.vrrom.application.model.Application;

import java.time.LocalDate;

public class AgreementMapper {
    public static AgreementInfo mapToAgreementInfo(Application application) {
        return AgreementInfo.builder()
                .pid(application.getId())
                .customerName(application.getCustomer().getName())
                .customerSurname(application.getCustomer().getSurname())
                .customerBirthDate(application.getCustomer().getBirthDate())
                .customerAddress(application.getCustomer().getAddress())
                .customerEmail(application.getCustomer().getEmail())
                .customerPhone(application.getCustomer().getPhone())
                .carMake(application.getVehicleDetails().getBrand())
                .carModel(application.getVehicleDetails().getModel())
                .carYear(application.getVehicleDetails().getYear())
                .leasingAmount(application.getPrice())
                .leasingYearPeriod(application.getYearPeriod())
                .monthlyPayment(application.getMonthlyPayment())
                .firstPaymentDate(LocalDate.of(2024, 6, 14)) //for now, change to dynamic variable later
                .interestRate(application.getInterestRate())
                .agreementDate(LocalDate.now())
                .build();
    }
}
