package com.vrrom.validation;

import java.time.LocalDateTime;

public class ValidationService {

    public static boolean isValidDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return true;}

        return startDate.isBefore(endDate) || startDate.isEqual(endDate);
    }
}
