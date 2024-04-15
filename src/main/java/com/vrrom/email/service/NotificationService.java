package com.vrrom.email.service;

public interface NotificationService {
    void notify(String messageSubject, String message, String emailRecipient);
}
