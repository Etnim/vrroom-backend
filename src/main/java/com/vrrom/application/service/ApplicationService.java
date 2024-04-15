package com.vrrom.application.service;

import com.vrrom.application.model.Application;
import com.vrrom.application.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }
}
