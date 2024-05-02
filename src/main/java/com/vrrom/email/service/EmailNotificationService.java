package com.vrrom.email.service;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
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
public class EmailNotificationService implements NotificationService {
    private static final Logger log = LogManager.getLogger(EmailNotificationService.class);
    private final String userEmail = "vrroom.leasing@gmail.com";
    private final Gmail gmailService;

    @Autowired
    public EmailNotificationService(Gmail gmailService) {
        this.gmailService = gmailService;
    }

    private static Session createSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.mime.address.strict", "false");

        // Session without an authenticator because we use OAuth2
        return Session.getInstance(properties);
    }

    private MimeMessage createMessage(String subject, String body, String recipientEmail) throws MessagingException {
        Session session = createSession();
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(userEmail));
        message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject(subject);
        message.setText(body);
        return message;
    }

    @Override
    public void notify(String subject, String body, String recipientEmail) {
        try {
            MimeMessage email = createMessage(subject, body, recipientEmail);
            sendEmail(email);
            log.info("Email sent successfully");
        } catch (MessagingException | IOException e) {
            log.error("Error sending email: {}", e.getMessage(), e);
        }
    }

    private void sendEmail(MimeMessage email) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        gmailService.users().messages().send("me", message).execute();
    }
}
