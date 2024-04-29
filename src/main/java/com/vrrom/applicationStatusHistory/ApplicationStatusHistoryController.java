package com.vrrom.applicationStatusHistory;

import com.vrrom.applicationStatusHistory.service.ApplicationStatusHistoryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.Duration;
import java.time.LocalDateTime;

@RestController
public class ApplicationStatusHistoryController {

    private final ApplicationStatusHistoryService applicationStatusHistoryService;

    public ApplicationStatusHistoryController(ApplicationStatusHistoryService applicationStatusHistoryService) {
        this.applicationStatusHistoryService = applicationStatusHistoryService;
    }

//    @GetMapping("/average-time-submit-sign")
//    public ResponseEntity<String> getAverageTimeFromSubmitToSign(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
//
//        Duration averageDuration = applicationStatusHistoryService.calculateAverageTimeFromSubmitToSignOrCancel(start, end);
//        long hours = averageDuration.toHours();
//        long minutes = averageDuration.toMinutesPart();
//        long seconds = averageDuration.toSecondsPart();
//
//        String averageTime = String.format("%d hours, %d minutes, %d seconds", hours, minutes, seconds);
//        return ResponseEntity.ok(averageTime);
//    }
}
