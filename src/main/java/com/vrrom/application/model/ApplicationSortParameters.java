package com.vrrom.application.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ApplicationSortParameters {
    APPLICATION_ID("applicationId", "id"),
    CUSTOMER_NAME("customerName", "customerName"),
    SURNAME("customerSurname", "customerSurname"),
    CREATED_DATE("applicationCreatedDate", "createdAt"),
    LEASING_AMOUNT("leasingAmount", "price"),
    APPLICATION_STATUS("applicationStatus", "applicationStatus"),
    MANAGER_ID("managerId", "managerId"),
    MANAGER_NAME("managerName", "managerName"),
    MANAGER_SURNAME("managerSurname", "managerSurname");

    private final String jsonValue;
    private final String displayName;

    ApplicationSortParameters(String jsonValue, String displayName) {
        this.jsonValue = jsonValue;
        this.displayName = displayName;
    }

    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    public String getDisplayName() {
        return displayName;
    }
}
