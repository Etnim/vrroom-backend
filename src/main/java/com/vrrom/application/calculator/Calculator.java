package com.vrrom.application.calculator;

import com.vrrom.application.model.Application;
import com.vrrom.customer.Customer;
import com.vrrom.euribor.dto.EuriborRate;

import java.math.BigDecimal;
import java.math.MathContext;

public class Calculator {
    public BigDecimal getDownPayment(BigDecimal price) {
        BigDecimal feeValue = price.divide(BigDecimal.valueOf(100));
        return feeValue.compareTo(BigDecimal.valueOf(200)) == 1 ? feeValue : BigDecimal.valueOf(200);
    }

    public BigDecimal getMonthlyPayment(Application application) {
        EuriborRate euribor = new EuriborRate();
        MathContext mc = new MathContext(5);

        BigDecimal price = application.getPrice();
        BigDecimal downPayment = application.getDownPayment();
        int residualValue = application.getResidualValue();
        int yearPeriod = application.getYearPeriod();
        double interestRate = application.getInterestRate();

        BigDecimal p = price.subtract(downPayment).subtract(BigDecimal.valueOf(residualValue));
        int n = yearPeriod * 12;
        double r = (interestRate / 100 + euribor.getRate()) / 12;
        BigDecimal interestWithPayment = BigDecimal.valueOf(Math.pow((1 + r), n));

        BigDecimal up = p.multiply(interestWithPayment).multiply(BigDecimal.valueOf(r));
        BigDecimal down = interestWithPayment.subtract(BigDecimal.ONE);

        return up.divide(down, mc);
    }

    public double getInterestRate(Customer customer) {
        return 2.0 + (customer.getCreditRating() - 1) * 1.5;
    }

}
