package com.vrrom.financialInfo.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EmploymentStatus {
    FULL_TIME("Full-time"),
    PART_TIME("Part-time"),
    SELF_EMPLOYED("Self-employed"),
    UNEMPLOYED("Unemployed");

    private final String employmentStatusText;

    EmploymentStatus(String employmentStatusText) {
        this.employmentStatusText = employmentStatusText;
    }
    @JsonValue
    public String getEmploymentStatusText() {
        return employmentStatusText;
    }
}
