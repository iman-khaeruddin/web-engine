package com.mospaze.werehouse.common;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.sms.MessageStatus;
import com.nexmo.client.sms.SmsSubmissionResponse;
import com.nexmo.client.sms.messages.TextMessage;
import com.nexmo.client.verify.CheckResponse;
import com.nexmo.client.verify.VerifyResponse;
import com.nexmo.client.verify.VerifyStatus;

public class NexmoExample {
    public static void main(String[] args) throws Exception {

        String NEXMO_API_KEY = "75af9177";
        String NEXMO_API_SECRET = "Yw4wwyVbwUo2RHRH";
        String TO_NUMBER = "6285921194491";
        String NEXMO_BRAND_NAME = "Mospaze";

        NexmoClient client = NexmoClient.builder().apiKey(NEXMO_API_KEY).apiSecret(NEXMO_API_SECRET).build();

        //SEND MESSAGE
        TextMessage message = new TextMessage(NEXMO_BRAND_NAME,
                TO_NUMBER,
                "A text message sent using the Nexmo SMS API"
        );

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            System.out.println("Message sent successfully.");
        } else {
            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
        }

        //SEND OTP (Verify)
        /*VerifyResponse verifyResponse = client.getVerifyClient().verify(TO_NUMBER, NEXMO_BRAND_NAME);

        if (verifyResponse.getStatus() == VerifyStatus.OK) {
            System.out.printf("RequestID: %s", verifyResponse.getRequestId());
        } else {
            System.out.printf("ERROR! %s: %s", verifyResponse.getStatus(), verifyResponse.getErrorText());
        }

        //Check verification code
        CheckResponse checkResponse = client.getVerifyClient().check(verifyResponse.getRequestId(), "0457");

        if (checkResponse.getStatus() == VerifyStatus.OK) {
            System.out.println("Verification Successful");
        } else {
            System.out.println("Verification failed: " + checkResponse.getErrorText());
        }*/
    }
}
