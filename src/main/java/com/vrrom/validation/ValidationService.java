package com.vrrom.validation;

import java.time.LocalDate;

public class ValidationService {

    public static boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return true;}

        return startDate.isBefore(endDate) || startDate.isEqual(endDate);
    }
}
