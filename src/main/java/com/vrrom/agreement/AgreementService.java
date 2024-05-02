package com.vrrom.agreement;

import com.vrrom.application.model.Application;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AgreementService {
    private final AgreementRepository agreementRepository;

    public AgreementService(AgreementRepository agreementRepository) {
        this.agreementRepository = agreementRepository;
    }

    public void saveAgreement(MultipartFile file, Application application) {
        try {
            Agreement agreement = new Agreement();
            agreement.setApplication(application);
            agreement.setAgreementContent(file.getBytes());
            agreement.setUploadDate(LocalDateTime.now());
            agreementRepository.save(agreement);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Agreement> getAgreementById(Long id) {
        return agreementRepository.findById(id);
    }
}
