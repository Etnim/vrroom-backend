package com.vrrom.application.util;

import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomPageWithHistory<T> extends CustomPageBase<T> {
    private Duration averageTimeToSignOrCancel;
    private long numberOfApplications;
    private Duration averageTimeFromSubmitToAssigned;
}



