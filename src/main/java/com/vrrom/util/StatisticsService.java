package com.vrrom.util;

import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.application.repository.ApplicationRepository;
import com.vrrom.applicationStatusHistory.model.ApplicationStatusHistory;
import com.vrrom.applicationStatusHistory.repository.ApplicationStatusHistoryRepository;
import com.vrrom.util.exceptions.StatisticsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Duration calculateAverageTimeFromSubmitToSignOrCancel(LocalDateTime start, LocalDateTime end, Optional<Long> managerIdOpt) throws StatisticsException {
        try {
            List<ApplicationStatusHistory> submittedHistories = applicationStatusHistoryRepository.findByStatusChangedAtBetweenAndManagerId(ApplicationStatus.SUBMITTED, start, end, managerIdOpt.orElse(null));
            long totalDuration = 0;
            int count = 0;
            for (ApplicationStatusHistory submitted : submittedHistories) {
                if (submitted.getApplication() == null || submitted.getChangedAt() == null) {
                    continue;
                }
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
        } catch (Exception e) {
            throw new StatisticsException("Failed to calculate average time", e.getCause());
        }
    }

    @Transactional
    public long countApplications(Optional<Long> managerIdOpt, LocalDateTime from, LocalDateTime to) throws StatisticsException {
        try {
            return applicationRepository.countByManagerIdAndDateRange(managerIdOpt.orElse(null), from, to);
        } catch (Exception e) {
            throw new StatisticsException("Failed to count applications", e.getCause());
        }
    }

    @Transactional
    public Duration calculateAverageTimeFromSubmitToAssigned(Optional<Long> managerIdOpt, LocalDateTime from, LocalDateTime to) throws StatisticsException {
        try {
            Double averageSeconds = applicationStatusHistoryRepository.findAverageTimeFromSubmittedToAssignedByManagerAndDateRange(managerIdOpt.orElse(null), from, to);
            return averageSeconds != null ? Duration.ofSeconds(averageSeconds.longValue()) : Duration.ZERO;
        } catch (Exception e) {
            throw new StatisticsException("Failed to calculate average time", e.getCause());
        }
    }
}
