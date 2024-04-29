package com.vrrom.application.mapper;

import com.vrrom.application.dto.ApplicationPage;
import com.vrrom.application.model.Application;

public class ApplicationPageMapper {
    public static ApplicationPage toApplicationListDTO(Application application) {
        ApplicationPage dto = ApplicationPage.builder()
                .applicationId(application.getId())
                .customerName(application.getCustomer().getName())
                .customerSurname(application.getCustomer().getSurname())
                .leasingAmount(application.getPrice())
                .applicationCreatedDate(application.getCreatedAt())
                .applicationStatus(application.getStatus())
                .build();
        if (application.getManager() != null) {
            dto.setManagerId(application.getManager().getId());
            dto.setManagerName(application.getManager().getName());
            dto.setManagerSurname(application.getManager().getSurname());
        } else {
            dto.setManagerId(null);
            dto.setManagerName(null);
            dto.setManagerSurname(null);
        }
        return dto;
    }
}
