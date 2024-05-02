package com.vrrom.applicationStatusHistory.model;

import com.vrrom.admin.Admin;
import com.vrrom.application.model.Application;
import com.vrrom.application.model.ApplicationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    @ManyToOne
    @JoinColumn(name = "changed_by_manager_id")
    private Admin manager;
    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    public ApplicationStatusHistory(Application application, ApplicationStatus status, Admin manager) {
        this.application = application;
        this.status = status;
        this.manager = manager;
        this.changedAt = LocalDateTime.now();
    }
}
