package com.vrrom.financialInfo.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EmploymentTerm {
    LESS_THAN_YEAR("Less than 1 year"),
    ONE_YEAR("1 year"),
    TWO_YEARS("2 years"),
    THREE_YEARS("3 years"),
    FOUR_YEARS("4 years"),
    FIVE_YEARS("5 years"),
    MORE_THAN_FIVE_YEARS("More than 5 years");

    private final String employmentTermText;

    EmploymentTerm(String employmentTermText) {
        this.employmentTermText = employmentTermText;
    }

    @JsonValue
    public String getEmploymentStatusText() {
        return employmentTermText;
    }
}
