package com.vrrom.util.validator;

import com.vrrom.util.annotation.ValidDateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, DateRangeDTO> {
    @Override
    public boolean isValid(DateRangeDTO dateRange, ConstraintValidatorContext context) {
        if (dateRange == null) {
            return true;
        }
        LocalDate startDate = dateRange.getStartDate();
        LocalDate endDate = dateRange.getEndDate();
        if (startDate == null || endDate == null) {
            return true;
        }

        return startDate.isBefore(endDate) || startDate.isEqual(endDate);
    }
}
