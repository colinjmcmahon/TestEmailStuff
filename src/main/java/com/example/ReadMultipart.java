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
                System.out.println("text/plain");
                returnAccessCode = stripAccessCode(result);
                System.out.println("Stripped Access Code: " + returnAccessCode);
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
                returnAccessCode = stripAccessCode(result);
                System.out.println("Stripped Access Code: " + returnAccessCode);
                //System.out.println("text/html");
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
                returnAccessCode = stripAccessCode(result);
                System.out.println("Stripped Access Code: " + returnAccessCode);
            }
        }
        return result;
    }

    public static String stripAccessCode(String result) {

        String fullEmail = result;

        int firstIndex = fullEmail.indexOf("Your code is: ");

//        System.out.println("First occurrence of char 'Your code is: '" +
//                " is found at : " + firstIndex);

        String accessCode = fullEmail.substring(firstIndex + 15, firstIndex + 21);

        return accessCode;
    }
}
