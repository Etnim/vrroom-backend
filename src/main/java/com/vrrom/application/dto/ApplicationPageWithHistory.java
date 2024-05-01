package com.vrrom.application.dto;

import com.vrrom.applicationStatusHistory.dto.ApplicationStatusHistoryDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class ApplicationPageWithHistory extends ApplicationPage {
    private List<ApplicationStatusHistoryDTO> statusHistory;
}
