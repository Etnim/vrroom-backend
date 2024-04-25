package com.vrrom.application.model;


import com.fasterxml.jackson.annotation.JsonValue;

public enum ApplicationStatus {
    SUBMITTED("Submitted"),
    UNDER_REVIEW("Under review"),
    PENDING_CHANGES("Pending changes"),
    PENDING_REVIEW("Pending review"),
    WAITING_FOR_SIGNING("Waiting for signing"),
    SIGNED("Signed"),
    REJECTED("Rejected"),
    CANCELLED("Canceled");
    private final String jsonValue;
    ApplicationStatus(String jsonValue) {
        this.jsonValue = jsonValue;
    }
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }
}
