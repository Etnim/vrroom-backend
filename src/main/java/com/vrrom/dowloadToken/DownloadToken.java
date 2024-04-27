package com.vrrom.dowloadToken;

import com.vrrom.application.model.Application;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DownloadToken {
    @Id
    @Column(name = "token", nullable = false)
    private UUID token;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "application_id")
    private Application application;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}
