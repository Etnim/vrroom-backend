package com.vrrom.agreement.mapper;

import com.vrrom.agreement.dto.AgreementInfo;
import com.vrrom.application.model.Application;

public class AgreementMapper {
    public static AgreementInfo mapToAgreementInfo(Application application) {
        return AgreementInfo
                .builder()
                .pid(application.getId())
                .customerName(application.getCustomer().getName())
                .customerSurname(application.getCustomer().getSurname())
                .customerBirthDate(application.getCustomer().getBirthDate())
                .customerAddress(application.getCustomer().getAddress())
                .customerEmail(application.getCustomer().getEmail())
                .customerPhone(application.getCustomer().getPhone())
                .carMake(application.getVehicleDetails().getMake())
                .carModel(application.getVehicleDetails().getModel())
                .carYear(application.getVehicleDetails().getYear())
                .leasingAmount(application.getFinancialInfo().getLeasingAmount())
                .downPayment(application.getFinancialInfo().getDownPayment())
                .leasingYearPeriod(application.getFinancialInfo().getLeasingYearPeriod())
                .residualValue(application.getFinancialInfo().getResidualValue())
                .agreementFee(application.getFinancialInfo().getAgreementFee())
                .build();
    }
}
