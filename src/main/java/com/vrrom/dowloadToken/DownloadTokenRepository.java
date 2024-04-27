package com.vrrom.dowloadToken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DownloadTokenRepository extends JpaRepository<DownloadToken, UUID> {
}
