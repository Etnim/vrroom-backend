package com.vrrom.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Setter
@Getter
public class ApplicationSearchParams {

    @Min(0)
    private int page = 0;

    @Max(20)
    private int size = 5;

    private String sortField = "applicationCreatedDate";

    private String sortDir = "desc";

    private Long managerId;

    private String status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
}
