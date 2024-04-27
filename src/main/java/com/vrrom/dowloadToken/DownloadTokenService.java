package com.vrrom.dowloadToken;

import com.vrrom.application.model.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DownloadTokenService {
    private final DownloadTokenRepository downloadTokenRepository;

    @Autowired
    public DownloadTokenService(DownloadTokenRepository downloadTokenRepository) {
        this.downloadTokenRepository = downloadTokenRepository;
    }

    public String generateToken(Application application) {
        UUID token = UUID.randomUUID();
        DownloadToken downloadToken = DownloadToken.builder().token(token).application(application).build();
        downloadTokenRepository.save(downloadToken);
        return token.toString();
    }

    public Long getApplicationId(String token) {
        return downloadTokenRepository.findById(UUID.fromString(token)).orElseThrow(() -> new IllegalArgumentException("Invalid token")).getApplication().getId();
    }
}
