package com.vrrom.applicationStatusHistory.service;

import com.vrrom.application.exception.ApplicationException;
import com.vrrom.application.model.Application;
import com.vrrom.applicationStatusHistory.model.ApplicationStatusHistory;
import com.vrrom.applicationStatusHistory.repository.ApplicationStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ApplicationStatusHistoryService {
    private final ApplicationStatusHistoryRepository applicationStatusHistoryRepository;

    @Autowired
    public ApplicationStatusHistoryService(ApplicationStatusHistoryRepository applicationStatusHistoryRepository) {
        this.applicationStatusHistoryRepository = applicationStatusHistoryRepository;
    }

    public void addApplicationStatusHistory(Application application) {
        ApplicationStatusHistory oldHistory = applicationStatusHistoryRepository.findByApplicationIdAndStatus(application.getId(), application.getStatus());
        if(oldHistory != null) {
            throw new ApplicationException("Application already in status " + application.getStatus().name());
        }
        ApplicationStatusHistory newHistory = ApplicationStatusHistory.builder()
                .application(application)
                .status(application.getStatus())
                .manager(application.getManager())
                .changedAt(LocalDateTime.now())
                .build();
        applicationStatusHistoryRepository.save(newHistory);
    }
}
