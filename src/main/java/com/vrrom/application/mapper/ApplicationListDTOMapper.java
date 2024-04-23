package com.vrrom.application.mapper;

import com.vrrom.application.model.Application;
import com.vrrom.application.dto.ApplicationListDTO;

public class ApplicationListDTOMapper {
    public static ApplicationListDTO toApplicationListDTO(Application application) {
        ApplicationListDTO dto = ApplicationListDTO.builder()
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
            dto.setManagerSurname(application.getManager().getName());
        } else {
            dto.setManagerId(null);
            dto.setManagerName(null);
            dto.setManagerSurname(null);
        }
        return dto;
    }
}
