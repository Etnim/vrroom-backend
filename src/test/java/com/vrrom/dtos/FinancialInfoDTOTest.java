package com.vrrom.dtos;

import com.vrrom.financialInfo.model.FinancialInfoDTO;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FinancialInfoDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testMonthlyIncomeValid() {
        FinancialInfoDTO info = new FinancialInfoDTO();
        info.setMonthlyIncome(new BigDecimal("500.00"));
        Set<ConstraintViolation<FinancialInfoDTO>> violations = validator.validateProperty(info, "monthlyIncome");
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testMonthlyIncomeInvalid() {
        FinancialInfoDTO info = new FinancialInfoDTO();
        info.setMonthlyIncome(new BigDecimal("0.00"));
        Set<ConstraintViolation<FinancialInfoDTO>> violations = validator.validateProperty(info, "monthlyIncome");
        assertEquals(1, violations.size());
    }

    @Test
    public void testMonthlyObligationsValid() {
        FinancialInfoDTO info = new FinancialInfoDTO();
        info.setMonthlyObligations(new BigDecimal("300.00"));
        Set<ConstraintViolation<FinancialInfoDTO>> violations = validator.validateProperty(info, "monthlyObligations");
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testMonthlyObligationsInvalid() {
        FinancialInfoDTO info = new FinancialInfoDTO();
        info.setMonthlyObligations(new BigDecimal("0.00"));
        Set<ConstraintViolation<FinancialInfoDTO>> violations = validator.validateProperty(info, "monthlyObligations");
        assertEquals(1, violations.size());
    }
    @Test
    public void testMaritalStatusNull() {
        FinancialInfoDTO info = new FinancialInfoDTO();
        info.setMaritalStatus(null);
        Set<ConstraintViolation<FinancialInfoDTO>> violations = validator.validateProperty(info, "maritalStatus");
        assertEquals(1, violations.size());
    }

    @Test
    public void testDependantsValid() {
        FinancialInfoDTO info = new FinancialInfoDTO();
        info.setDependants(2);
        Set<ConstraintViolation<FinancialInfoDTO>> violations = validator.validateProperty(info, "dependants");
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testDependantsInvalid() {
        FinancialInfoDTO info = new FinancialInfoDTO();
        info.setDependants(-1);
        Set<ConstraintViolation<FinancialInfoDTO>> violations = validator.validateProperty(info, "dependants");
        assertEquals(1, violations.size());
    }
}
