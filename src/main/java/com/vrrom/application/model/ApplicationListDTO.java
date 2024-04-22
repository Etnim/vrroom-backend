package com.vrrom.application.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ApplicationListDTO {
    private long applicationId;
    private String customerName;
    private String customerSurname;
    private BigDecimal leasingAmount;
    private LocalDate applicationCreatedDate;
    private ApplicationStatus applicationStatus;
    private long managerId;
    private String managerName;
    private String managerSurname;
}
