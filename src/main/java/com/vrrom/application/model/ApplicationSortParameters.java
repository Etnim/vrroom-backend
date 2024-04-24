package com.vrrom.application.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum ApplicationSortParameters {
    APPLICATION_ID("applicationId", "id"),
    CUSTOMER_NAME("customerName", "customerName"),
    SURNAME("customerSurname", "customerSurname"),
    CREATED_DATE("applicationCreatedDate", "createdAt"),
    LEASING_AMOUNT("leasingAmount", "price"),
    APPLICATION_STATUS("applicationStatus", "status"),
    MANAGER_ID("managerId", "managerId"),
    MANAGER_NAME("managerName", "managerName"),
    MANAGER_SURNAME("managerSurname", "managerSurname");
    private static final Map<String, ApplicationSortParameters> BY_VALUE = Arrays.stream(values())
            .collect(Collectors.toMap(ApplicationSortParameters::getValue, e -> e));
    private final String requestValue;
    private final String value;

    ApplicationSortParameters(String jsonValue, String displayName) {
        this.requestValue = jsonValue;
        this.value = displayName;
    }

    @JsonValue
    public String getRequestValue() {
        return requestValue;
    }

    public String getValue() {
        return value;
    }

    public static ApplicationSortParameters fromDisplayName(String displayName) {
        return BY_VALUE.get(displayName);
    }
}
