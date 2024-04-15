package com.vrrom.financialInfo.repository;

import com.vrrom.financialInfo.model.FinancialInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialInfoRepository extends JpaRepository<FinancialInfo, Long> {
}
