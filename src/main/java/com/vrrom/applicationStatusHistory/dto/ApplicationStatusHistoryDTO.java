package com.vrrom.applicationStatusHistory.dto;

import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.applicationStatusHistory.model.ApplicationStatusHistory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ApplicationStatusHistoryDTO {
 private long applicationId;
 private ApplicationStatus status;
 private Long managerId;
 private LocalDateTime changedAt;
}
