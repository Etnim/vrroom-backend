package com.vrrom.financialInfo.service;

import com.vrrom.financialInfo.repository.FinancialInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinancialInfoService {
    private final FinancialInfoRepository financialInfoRepository;

    @Autowired
    public FinancialInfoService(FinancialInfoRepository financialInfoRepository) {
        this.financialInfoRepository = financialInfoRepository;
    }
}
