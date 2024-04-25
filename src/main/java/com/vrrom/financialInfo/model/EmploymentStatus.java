package com.vrrom.financialInfo.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EmploymentStatus {
    FULL_TIME("Full-time"),
    PART_TIME("Part-time"),
    SELF_EMPLOYED("Self-employed"),
    UNEMPLOYED("Unemployed");

    private final String jsonValue;

    EmploymentStatus(String jsonValue) {
        this.jsonValue = jsonValue;
    }
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }
}
