package com.vrrom.application.mapper;

import com.vrrom.application.model.Application;
import com.vrrom.application.model.ApplicationListDTO;

public class ApplicationListDTOMapper {
    public static ApplicationListDTO toApplicationListDTO(Application application) {
        return ApplicationListDTO.builder()
                .applicationId(application.getId())
                .customerName(application.getCustomer().getName())
                .customerSurname(application.getCustomer().getSurname())
                .leasingAmount(application.getPrice())
                .applicationCreatedDate(application.getCreatedAt())
                .applicationStatus(application.getStatus())
                .managerId(application.getManager().getId())
                .managerName(application.getManager().getName())
                .managerSurname(application.getManager().getSurname())
                .build();
    }
}
