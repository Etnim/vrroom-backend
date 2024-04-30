package com.vrrom.application.dto;

import com.vrrom.application.model.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class ApplicationPage {
    private long applicationId;
    private String customerName;
    private String customerSurname;
    private BigDecimal leasingAmount;
    private LocalDateTime applicationCreatedDate;
    private ApplicationStatus applicationStatus;
    private Long managerId;
    private String managerName;
    private String managerSurname;
}
