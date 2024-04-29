package com.vrrom.application.calculator;

import com.vrrom.application.dto.ApplicationRequest;
import com.vrrom.customer.Customer;
import com.vrrom.euribor.dto.EuriborRate;

import java.math.BigDecimal;
import java.math.MathContext;

public class Calculator {
    public final MathContext mc = new MathContext(5);

    public BigDecimal getAgreementFee(BigDecimal price) {
        BigDecimal feeValue = price.divide(BigDecimal.valueOf(100), mc);
        return feeValue
                .compareTo(BigDecimal.valueOf(200)) > 0
                ? feeValue
                : BigDecimal.valueOf(200);
    }

    public BigDecimal getDownPayment(ApplicationRequest application){
        BigDecimal downPaymentPercentage = BigDecimal.valueOf(application.getDownPayment());
        return downPaymentPercentage
                .multiply(application.getPrice())
                .divide(BigDecimal.valueOf(100), mc);
    }

    public BigDecimal getMonthlyPayment(ApplicationRequest application, Customer customer) {
        EuriborRate euribor = new EuriborRate();
        BigDecimal price = application.getPrice();
        BigDecimal downPayment = getDownPayment(application);
        BigDecimal residualValue = getResidualValue(application);
        int yearPeriod = application.getYearPeriod();
        double interestRate = getInterestRate(customer);

        BigDecimal p = price.subtract(downPayment).subtract(residualValue);
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

    public BigDecimal getResidualValue(ApplicationRequest application){
        BigDecimal residualValuePercentage = BigDecimal.valueOf(application.getResidualValue());

       return residualValuePercentage
               .multiply(application.getPrice())
               .divide(BigDecimal.valueOf(100), mc);
    }
}
