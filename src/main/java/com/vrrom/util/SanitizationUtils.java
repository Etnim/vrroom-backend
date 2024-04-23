package com.vrrom.util;

import com.vrrom.application.model.ApplicationStatus;
import java.util.Arrays;
import java.util.List;

public class SanitizationUtils {
    public static String sanitizeCarMake(String make) {
        return make.replaceAll("[^a-zA-Z0-9\\s]", "");
    }

    public static boolean isValidSortField(String sortField, String sortDir) {
        List<String> validSortFields = List.of("applicationId", "customerName", "surname", "applicationCreatedDate", "leasingAmount", "applicationStatus", "managerId", "managerName", "managerSurname");
        return validSortFields.contains(sortField) && ("asc".equalsIgnoreCase(sortDir) || "desc".equalsIgnoreCase(sortDir));
    }

    public static boolean isApplicationStatus(String status) {
        return Arrays.stream(ApplicationStatus.values())
                .anyMatch(e -> e.name().equals(status));
    }
}
