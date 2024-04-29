package com.vrrom.application.util;

import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.application.repository.ApplicationRepository;
import com.vrrom.applicationStatusHistory.model.ApplicationStatusHistory;
import com.vrrom.applicationStatusHistory.repository.ApplicationStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StatisticsService {

    private final ApplicationStatusHistoryRepository applicationStatusHistoryRepository;
    private final ApplicationRepository applicationRepository;


    @Autowired
    public StatisticsService(ApplicationStatusHistoryRepository applicationStatusHistoryRepository, ApplicationRepository applicationRepository) {
        this.applicationStatusHistoryRepository = applicationStatusHistoryRepository;
        this.applicationRepository = applicationRepository;
    }

    public Duration calculateAverageTimeFromSubmitToSignOrCancel(LocalDateTime start, LocalDateTime end, Optional<Long> managerIdOpt) {
        List<ApplicationStatusHistory> submittedHistories = applicationStatusHistoryRepository.findByStatusChangedAtBetweenAndManagerId(ApplicationStatus.SUBMITTED, start, end, managerIdOpt.orElse(null));
        long totalDuration = 0;
        int count = 0;
        for (ApplicationStatusHistory submitted : submittedHistories) {
            Long applicationId = submitted.getApplication().getId();
            Optional<ApplicationStatusHistory> signed = applicationStatusHistoryRepository.findFirstByApplicationIdAndStatusAndDateAfterAndManagerId(applicationId, ApplicationStatus.SIGNED, submitted.getChangedAt(), managerIdOpt.orElse(null));
            Optional<ApplicationStatusHistory> cancelled = applicationStatusHistoryRepository.findFirstByApplicationIdAndStatusAndDateAfterAndManagerId(applicationId, ApplicationStatus.CANCELLED, submitted.getChangedAt(), managerIdOpt.orElse(null));
            LocalDateTime earliest = null;
            if (signed.isPresent() && cancelled.isPresent()) {
                earliest = signed.get().getChangedAt().isBefore(cancelled.get().getChangedAt()) ? signed.get().getChangedAt() : cancelled.get().getChangedAt();
            } else if (signed.isPresent()) {
                earliest = signed.get().getChangedAt();
            } else if (cancelled.isPresent()) {
                earliest = cancelled.get().getChangedAt();
            }
            if (earliest != null) {
                totalDuration += Duration.between(submitted.getChangedAt(), earliest).getSeconds();
                count++;
            }
        }
        return count > 0 ? Duration.ofSeconds(totalDuration / count) : Duration.ZERO;
    }

    public long countApplications(Optional<Long> managerIdOpt, LocalDateTime from, LocalDateTime to) {
        return applicationRepository.countByManagerIdAndDateRange(managerIdOpt.orElse(null), from, to);
    }

    public Duration calculateAverageTimeFromSubmitToAssigned(Optional<Long> managerIdOpt, LocalDateTime from, LocalDateTime to) {
        Double averageSeconds = applicationStatusHistoryRepository.findAverageTimeFromSubmittedToAssignedByManagerAndDateRange(managerIdOpt.orElse(null), from, to);
        return averageSeconds != null ? Duration.ofSeconds(averageSeconds.longValue()) : Duration.ZERO;
    }
}
