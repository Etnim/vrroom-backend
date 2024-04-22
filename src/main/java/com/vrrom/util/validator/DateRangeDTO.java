package com.vrrom.util.validator;

import com.vrrom.util.annotation.ValidDateRange;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Optional;

@ValidDateRange
public class DateRangeDTO {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    public LocalDate getStartDate() {
        return startDate;
    }


    public LocalDate getEndDate() {
        return endDate;
    }

@Bean
    public static LocalDate extractStartDate(DateRangeDTO dateRange) {
        Optional<DateRangeDTO> optionalDateRange = Optional.ofNullable(dateRange);
    return optionalDateRange.map(DateRangeDTO::getStartDate).orElse(null);

    }
    public static LocalDate extractEndDate(DateRangeDTO dateRange) {
        Optional<DateRangeDTO> optionalDateRange = Optional.ofNullable(dateRange);
        return optionalDateRange.map(DateRangeDTO::getEndDate).orElse(null);

    }

}
