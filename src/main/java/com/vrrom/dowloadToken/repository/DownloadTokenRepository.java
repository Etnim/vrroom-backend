package com.vrrom.dowloadToken.repository;

import com.vrrom.dowloadToken.model.DownloadToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DownloadTokenRepository extends JpaRepository<DownloadToken, UUID> {
}
