package com.vrrom.financialInfo.mapper;

import com.vrrom.application.model.Application;
import com.vrrom.financialInfo.dtos.FinancialInfoResponse;
import com.vrrom.financialInfo.model.FinancialInfo;
import com.vrrom.financialInfo.dtos.FinancialInfoRequest;

public class FinancialInfoMapper {
    public static FinancialInfo toEntity(FinancialInfoRequest financialInfoRequest, Application application) {
        return  new FinancialInfo.Builder()
                .withFinancialInfoDTO(financialInfoRequest)
                .withApplication(application)
                .build();
    }

    public static FinancialInfoResponse toResponse(FinancialInfo financialInfo){
        FinancialInfoResponse response = new FinancialInfoResponse();
        response.setMonthlyIncome(financialInfo.getMonthlyIncome());
        response.setMaritalStatus(financialInfo.getMaritalStatus().getMaritalStatusText());
        response.setDependants(financialInfo.getDependants());
        response.setMonthlyObligations(financialInfo.getMonthlyObligations());
        response.setDisposableIncome(financialInfo.calculateDisposableIncome());
        response.setEmploymentStatus(financialInfo.getEmploymentStatus().getEmploymentStatusText());
        return response;
    }
}
