package com.vrrom.email.service;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.vrrom.email.exception.EmailServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

@Service
public class EmailService implements NotificationService {
    private static final Logger log = LogManager.getLogger(EmailService.class);
    private final Gmail gmailService;

    @Autowired
    public EmailService(Gmail gmailService) {
        this.gmailService = gmailService;
    }

    private static Session createSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.mime.address.strict", "false");
        return Session.getInstance(properties);
    }

    private MimeMessage createMessage(String subject, String body, String recipientEmail) throws MessagingException {
        Session session = createSession();
        MimeMessage message = new MimeMessage(session);
        String userEmail = "vrroom.leasing@gmail.com";
        message.setFrom(new InternetAddress(userEmail));
        message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject(subject);
        message.setText(body);
        return message;
    }

    @Override
    public void notify(String subject, String body, String recipientEmail) throws EmailServiceException {
        try {
            MimeMessage email = createMessage(subject, body, recipientEmail);
            sendEmail(email);
            log.info("Email sent successfully");
        } catch (MessagingException | IOException e) {
            throw new EmailServiceException("Error sending email", e.getCause());
        }
    }

    private void sendEmail(MimeMessage email) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = java.util.Base64.getUrlEncoder().encodeToString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        gmailService.users().messages().send("me", message).execute();
    }
}
