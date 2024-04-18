package com.vrrom.financialInfo.mapper;

import com.vrrom.application.model.Application;
import com.vrrom.financialInfo.model.FinancialInfo;
import com.vrrom.financialInfo.model.FinancialInfoDTO;

public class FinancialInfoMapper {
    public static FinancialInfo toEntity(FinancialInfoDTO financialInfoDTO, Application application) {
        return  new FinancialInfo.Builder()
                .withFinancialInfoDTO(financialInfoDTO)
                .withApplication(application)
                .build();
    }
}
