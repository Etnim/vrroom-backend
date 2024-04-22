package com.vrrom.application.util;


import com.vrrom.application.dto.ApplicationSearchParams;
import com.vrrom.util.SanitizationUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class PaginatedApplicationsValidator implements ConstraintValidator<ValidPaginatedApplications, ApplicationSearchParams> {
    private static final int MAX_PAGE_SIZE = 20;

    @Override
    public boolean isValid(ApplicationSearchParams params, ConstraintValidatorContext context) {
        if (params == null) {
            return true;
        }

        int page = params.getPage();
        int size = params.getSize();
        String sortField = params.getSortField();
        String sortDir = params.getSortDir();
        Long managerId = params.getManagerId();
        String status = params.getStatus();
        LocalDate startDate = params.getStartDate();
        LocalDate endDate = params.getEndDate();

        if (page < 0 || size > MAX_PAGE_SIZE) {
            return false;
        }
        if (!SanitizationUtils.isValidSortField(sortField, sortDir)) {
            return false;
        }
        if (startDate != null && endDate != null && !startDate.isBefore(endDate)) {
            return false;
        }
        if (status != null && !SanitizationUtils.isApplicationStatus(status)) {
            return false;
        }
        if (managerId != null && managerId < 0) {
            return false;
        }
        return true;
    }
}
