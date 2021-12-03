package com.example;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.*;

public class ReadMultipart {

    public static String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        String returnAccessCode = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("TEXT/HTML")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }

    public static String stripAccessCode(String result) {

        String fullEmail = result;
        int firstIndex = fullEmail.indexOf("Your code is: ");
        String accessCode = fullEmail.substring(firstIndex + 14, firstIndex + 21);
        accessCode.trim();
        return accessCode;
    }


}
