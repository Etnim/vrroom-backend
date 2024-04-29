package com.vrrom.applicationStatusHistory.service;

import com.vrrom.application.dto.ApplicationListDTO;
import com.vrrom.application.dto.ApplicationListDTOWithHistory;
import com.vrrom.applicationStatusHistory.dto.ApplicationStatusHistoryDTO;
import com.vrrom.applicationStatusHistory.mapper.ApplicationStatusHistoryMapper;
import com.vrrom.applicationStatusHistory.model.ApplicationStatusHistory;
import com.vrrom.applicationStatusHistory.repository.ApplicationStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationStatusHistoryService {
    private final ApplicationStatusHistoryRepository applicationStatusHistoryRepository;

    @Autowired
    public ApplicationStatusHistoryService(ApplicationStatusHistoryRepository applicationStatusHistoryRepository) {
        this.applicationStatusHistoryRepository = applicationStatusHistoryRepository;
    }

    public List<ApplicationStatusHistory> getApplicationStatusHistory(Long applicationId) {
        return applicationStatusHistoryRepository.findByApplicationId(applicationId);
    }

    public ApplicationListDTOWithHistory enhanceDtoWithHistory(ApplicationListDTO dto) {
        List<ApplicationStatusHistory> history = getApplicationStatusHistory(dto.getApplicationId());
        List<ApplicationStatusHistoryDTO> historyDTOs = history.stream()
                .map(ApplicationStatusHistoryMapper::toApplicationStatusHistoryDTO)
                .collect(Collectors.toList());
        return ApplicationListDTOWithHistory.builder()
                .applicationId(dto.getApplicationId())
                .customerName(dto.getCustomerName())
                .customerSurname(dto.getCustomerSurname())
                .leasingAmount(dto.getLeasingAmount())
                .applicationCreatedDate(dto.getApplicationCreatedDate())
                .applicationStatus(dto.getApplicationStatus())
                .managerId(dto.getManagerId())
                .managerName(dto.getManagerName())
                .managerSurname(dto.getManagerSurname())
                .statusHistory(historyDTOs)
                .build();
    }
}
