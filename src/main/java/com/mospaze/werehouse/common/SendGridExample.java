package com.mospaze.werehouse.common;

import com.sendgrid.*;

import java.io.IOException;

public class SendGridExample {
    public static void main(String[] args) throws IOException {
        Email from = new Email("test@example.com");
        String subject = "Sending with SendGrid is Fun";
        Email to = new Email("iman.khaeruddin@mospaze.com");
        Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("respose code : " + response.getStatusCode());
            System.out.println("response body : " + response.getBody());
            System.out.println("response header : " + response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }
}