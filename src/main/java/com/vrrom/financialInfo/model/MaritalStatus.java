package com.vrrom.financialInfo.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MaritalStatus {
    SINGLE("Single"),
    MARRIED("Married"),
    DIVORCED("Divorced"),
    COHABITEE("Cohabitee");

    private final String maritalStatusText;

    MaritalStatus(String maritalStatusText) {
        this.maritalStatusText = maritalStatusText;
    }

    @JsonValue
    public String getMaritalStatusText() {
        return maritalStatusText;
    }
}
