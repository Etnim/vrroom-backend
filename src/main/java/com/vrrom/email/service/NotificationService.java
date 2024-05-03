package com.vrrom.email.service;

import com.vrrom.email.exception.EmailServiceException;

public interface NotificationService {
    void notify(String messageSubject, String message, String emailRecipient) throws EmailServiceException;
}
