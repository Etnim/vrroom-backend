package com.vrrom.email;

import com.vrrom.email.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;

@SpringBootTest
public class EmailServiceTest {
    @Value("${mailgun.api.key}")
    private String apiKey;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testSendEmail() {
        String from = "vrroom.leasing@gmail.com";
        String to = "vrroom.leasing@gmail.com";
        String subject = "Test Subject";
        String text = "Hello, this is a test email.";
        String basicAuthValue = "Basic " + Base64.getEncoder().encodeToString(("api:" + apiKey).getBytes(StandardCharsets.UTF_8));
        String expectedResponse = "{\"message\":\"Queued. Thank you.\"}";
        mockServer.expect(once(), MockRestRequestMatchers.requestTo("https://api.mailgun.net/v3/sandbox34ec216cc5bd4b9cbab0c14d81333572.mailgun.org/messages"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string("from=vrroom.leasing@gmail.com&to=vrroom.leasing@gmail.com&subject=Test Subject&text=Hello, this is a test email."))
                .andExpect(MockRestRequestMatchers.header("Authorization", basicAuthValue))  // Basic auth encoding of "api:your-mailgun-api-key"
                .andRespond(MockRestResponseCreators.withSuccess(expectedResponse, MediaType.APPLICATION_JSON));
        String response = emailService.sendEmail(from, to, subject, text);
        assertEquals(expectedResponse, response);
        mockServer.verify();
    }
}
