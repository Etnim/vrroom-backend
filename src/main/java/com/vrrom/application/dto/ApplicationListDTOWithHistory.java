package com.vrrom.application.dto;

import com.vrrom.applicationStatusHistory.dto.ApplicationStatusHistoryDTO;
import com.vrrom.applicationStatusHistory.model.ApplicationStatusHistory;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class ApplicationListDTOWithHistory extends ApplicationListDTO {
    private List<ApplicationStatusHistoryDTO> statusHistory;


}
