package com.vrrom.applicationStatusHistory.mapper;

import com.vrrom.applicationStatusHistory.dto.ApplicationStatusHistoryDTO;
import com.vrrom.applicationStatusHistory.model.ApplicationStatusHistory;

public class ApplicationStatusHistoryMapper {
    public static ApplicationStatusHistoryDTO toApplicationStatusHistoryDTO(ApplicationStatusHistory applicationStatusHistory) {
        return ApplicationStatusHistoryDTO
                .builder()
                .applicationId(applicationStatusHistory.getApplication().getId())
                .status(applicationStatusHistory.getStatus())
                .managerId(applicationStatusHistory.getManager().getId())
                .changedAt(applicationStatusHistory.getChangedAt())
                .build();
    }
}
