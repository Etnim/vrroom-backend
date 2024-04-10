package com.vrrom.model;

import java.math.BigInteger;
import java.util.Objects;

public class Application {
    private BigInteger amount;
    private int downPayment;
    private int residualValue;
    private int period;
    private double interestRate;

    private EmploymentStatus employmentStatus;
    private BigInteger monthlyIncome;
    private int dependants;
    private boolean monthlyObligations;
    private Vehicle vehicle;
    private Customer customer;

    @Override
    public String toString() {
        return "Application{" +
                "amount=" + amount +
                ", downPayment=" + downPayment +
                ", residualValue=" + residualValue +
                ", period=" + period +
                ", interestRate=" + interestRate +
                ", employmentStatus=" + employmentStatus +
                ", monthlyIncome=" + monthlyIncome +
                ", dependants=" + dependants +
                ", monthlyObligations=" + monthlyObligations +
                ", vehicle=" + vehicle +
                ", customer=" + customer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Application that)) return false;
        return downPayment == that.downPayment && residualValue == that.residualValue && period == that.period && Double.compare(interestRate, that.interestRate) == 0 && dependants == that.dependants && monthlyObligations == that.monthlyObligations && Objects.equals(amount, that.amount) && employmentStatus == that.employmentStatus && Objects.equals(monthlyIncome, that.monthlyIncome) && vehicle == that.vehicle && Objects.equals(customer, that.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, downPayment, residualValue, period, interestRate, employmentStatus, monthlyIncome, dependants, monthlyObligations, vehicle, customer);
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public int getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(int downPayment) {
        this.downPayment = downPayment;
    }

    public int getResidualValue() {
        return residualValue;
    }

    public void setResidualValue(int residualValue) {
        this.residualValue = residualValue;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public BigInteger getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigInteger monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public int getDependants() {
        return dependants;
    }

    public void setDependants(int dependants) {
        this.dependants = dependants;
    }

    public boolean isMonthlyObligations() {
        return monthlyObligations;
    }

    public void setMonthlyObligations(boolean monthlyObligations) {
        this.monthlyObligations = monthlyObligations;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Application(BigInteger amount, int downPayment, int residualValue, int period, double interestRate, EmploymentStatus employmentStatus, BigInteger monthlyIncome, int dependants, boolean monthlyObligations, Vehicle vehicle, Customer customer) {
        this.amount = amount;
        this.downPayment = downPayment;
        this.residualValue = residualValue;
        this.period = period;
        this.interestRate = interestRate;
        this.employmentStatus = employmentStatus;
        this.monthlyIncome = monthlyIncome;
        this.dependants = dependants;
        this.monthlyObligations = monthlyObligations;
        this.vehicle = vehicle;
        this.customer = customer;
    }
}
