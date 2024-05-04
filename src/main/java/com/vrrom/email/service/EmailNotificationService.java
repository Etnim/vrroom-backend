package com.vrrom.email.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailNotificationService implements NotificationService {
    private final String userEmail = "vrroom.leasing@gmail.com";
    private MimeMessage message;

    String accessToken= "ihmo onnl flfd sovb";
    private static final Logger log = LogManager.getLogger(EmailNotificationService.class);

    @Autowired
    public EmailNotificationService() {
        Session session = createSession(userEmail, accessToken);
        this.message = new MimeMessage(session);
    }

    public static Session createSession(String userEmail, String accessToken) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.mime.address.strict", "false");
        javax.mail.Authenticator authenticator = new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(userEmail, accessToken);
            }
        };
        return Session.getInstance(properties, authenticator);
    }

    public MimeMessage createMessage(String messageSubject, String messageBody, String recipientEmail) throws MessagingException {
        message.setFrom(new InternetAddress(userEmail));
        message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(messageSubject);
        message.setText(messageBody);
        return message;
    }

    @Override
    public void notify(String messageSubject, String messageBody, String recipientEmail) {
        try {
            MimeMessage email = createMessage(messageSubject, messageBody, recipientEmail);
            sendEmail(email);
            log.info("Email sent successfully}");
        } catch (MessagingException e) {
            log.error("Error sending email: {}", e.getMessage());
        }
    }

    public void sendEmail(MimeMessage message) throws MessagingException {
        Transport.send(message);
    }
}

