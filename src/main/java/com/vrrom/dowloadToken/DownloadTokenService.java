package com.vrrom.dowloadToken;

import com.vrrom.application.model.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        DownloadToken downloadToken = DownloadToken.builder()
                .token(token)
                .application(application).
                expiresAt(LocalDateTime.now().plusDays(10))
                .build();
        downloadTokenRepository.save(downloadToken);
        return token.toString();
    }

    public Long getApplicationId(String token) throws DownloadTokenException {
        DownloadToken downloadToken = downloadTokenRepository.findById(UUID.fromString(token)).orElseThrow(() -> new DownloadTokenException("Unable to download agreement. ", new Throwable("Invalid credentials")));
        if (downloadToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            downloadTokenRepository.delete(downloadToken);
            throw new DownloadTokenException("Unable to download agreement. ", new Throwable("Agreement is expired"));
        }
        return downloadToken.getApplication().getId();
    }
}
