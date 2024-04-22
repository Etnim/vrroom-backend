package com.vrrom.application.mapper;

import com.vrrom.admin.Admin;
import com.vrrom.application.model.Application;
import com.vrrom.application.dtos.ApplicationListDTO;

public class ApplicationListDTOMapper {
    public static ApplicationListDTO toApplicationListDTO(Application application, Admin manager) {
        return ApplicationListDTO.builder()
                .applicationId(application.getId())
                .name(application.getCustomer().getName())
                .surname(application.getCustomer().getSurname())
                .leasingAmount(application.getPrice())
                .applicationCreatedDate(application.getCreatedAt())
                .applicationStatus(application.getStatus())
                .managerId(application.getManager().getId())
                .managerName(application.getManager().getName())
                .managerSurname(application.getManager().getSurname())
                .build();
    }
}
