package com.vrrom.application.mapper;

import com.vrrom.application.model.AgreementInfo;
import com.vrrom.application.model.Application;

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
                .downPayment(application.getDownPayment())
                .leasingYearPeriod(application.getYearPeriod())
                .residualValue(application.getResidualValue())
                .agreementFee(application.getAgreementFee())
                .build();
    }
}
