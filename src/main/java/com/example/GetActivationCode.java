package com.example;

import java.io.IOException;
import java.util.Properties;

import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import org.jsoup.Jsoup.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.example.ReadMultipart;


public class GetActivationCode {


    public static void check(String host, String storeType, String user, String password) {
        try {
            String returnAccessCode = "";
            // create properties
            Properties properties = new Properties();

            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.starttls.enable", "true");
            properties.put("mail.imap.ssl.trust", host);

            Session emailSession = Session.getDefaultInstance(properties);

            // create the imap store object and connect to the imap server
            Store store = emailSession.getStore("imaps");

            store.connect(host, user, password);
            // create the inbox object and open it
            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
            System.out.println("messages.length---" + messages.length);
            String result = "";

            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                message.setFlag(Flag.SEEN, true);
//                System.out.println("---------------------------------");
//                System.out.println("Email Number " + (i + 1));
//                System.out.println("Subject: " + message.getSubject());
//                System.out.println("From: " + message.getFrom()[0]);
                //System.out.println("Text: " + message.getContent().toString());


                if (message.isMimeType("text/plain")) {
                    result = message.getContent().toString();
                } else if (message.isMimeType("multipart/*")) {
                    MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                    result = ReadMultipart.getTextFromMimeMultipart(mimeMultipart);
                }else{
                    if(message.isMimeType("TEXT/HTML")){
                        String html = (String) message.getContent();
                        result = org.jsoup.Jsoup.parse(html).text();
                        returnAccessCode = ReadMultipart.stripAccessCode(result);
                        System.out.println("Activation Code: " + returnAccessCode);
                    }else {
                        System.out.println("message is neither multipart,text/plain or text/html..");
                    }
                }

            }

            inbox.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

//        //for gmail account
        String host = "imap.gmail.com";
        String mailStoreType = "imap";
        String username = "iaaunregistered1@gmail.com";
        String password = "hbucnvuiesgfdqdd";

        //String password = "fjvlqxpuytnorjqn";

        //for outlook account:
//        String host = "outlook.office365.com";
//        String mailStoreType = "imap";
//        String username = "iaatestacc3@outlook.com";
//        String password = "khzermbnhifyesad";

        check(host, mailStoreType, username, password);

        //String returnEmail = ExcelHelper.fetchNextUnusedEmail();

    }
}