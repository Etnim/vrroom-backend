package com.vrrom.messageSender;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {
    @Value("${twilio.account.sid}")
    private  String ACCOUNT_SID;
    @Value("${twilio.auth.token}")
    private  String AUTH_TOKEN;
    @Value("${twilio.number}")
    private  String TWILIO_NUMBER;

    public void sendMessage(String sms, String number) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        try {
            Message message = Message
                    .creator(
                            new PhoneNumber(number),
                            new PhoneNumber(TWILIO_NUMBER),
                            sms
                    )
                    .create();

        } catch (ApiException e) {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            System.err.println(e);
        }
    }
}