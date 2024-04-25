package com.vrrom.financialInfo.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MaritalStatus {
    SINGLE("Single"),
    MARRIED("Married"),
    DIVORCED("Divorced"),
    COHABITEE("Cohabitee");

    MaritalStatus(String jsonValue) {
        this.jsonValue = jsonValue;
    }
    private final String jsonValue;
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }
}
