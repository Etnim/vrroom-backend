package com.vrrom.email;

import com.vrrom.email.service.EmailNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class EmailNotificationServiceTest {
    @Mock
    private Session session;
    @Mock
    private MimeMessage mimeMessage;
    @Spy
    @InjectMocks
    private EmailNotificationService emailNotificationService;
    String subject = "Test Subject";
    String body = "Test Body";
    String recipient = "vrroom.leasing@gmail.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void propertiesShouldNotBeNullWhenCreatingSession() {
        Session session = EmailNotificationService.createSession("user@example.com", "password");
        assertNotNull(session.getProperties());
        assertNotNull(session.getProperties().getProperty("mail.smtp.auth"));
        assertEquals("true", session.getProperties().getProperty("mail.smtp.auth"));
    }

    @Test
    void testNotify() throws Exception {
        emailNotificationService.notify(subject, body, recipient);
        verify(mimeMessage).setFrom(any(InternetAddress.class));
        verify(mimeMessage).setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(recipient));
        verify(mimeMessage).setSubject(subject);
        verify(mimeMessage).setText(body);
    }
}
