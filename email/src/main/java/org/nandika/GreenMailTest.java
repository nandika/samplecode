package org.nandika;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class GreenMailTest {

    public static void main(String[] args) throws InterruptedException, IOException, MessagingException {
        ServerSetup setup = new ServerSetup(3025, "localhost", "smtp");
        GreenMail greenMail = new GreenMail(setup);
        greenMail.setUser("user1@mail.com", "user1", "user1");
        greenMail.start();

        final String username = "user1";
        final String password = "user1";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "localhost");
        props.put("mail.smtp.port", "3025");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication(username, password);
              }
          });

        Message message = new MimeMessage(session);
        message.setSubject("Mail Subject");
        message.setText("Mail content");
        message.setFrom(new InternetAddress("user1@mail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("user1@mail.com"));
        Transport.send(message);

        greenMail.waitForIncomingEmail(5000, 1);
        Message[] messages = greenMail.getReceivedMessages();
        System.out.println("Message length =>" + messages.length);
        System.out.println("Subject => " + messages[0].getSubject());
        System.out.println("Content => " + messages[0].getContent().toString());
        System.out.println("Done");
        greenMail.stop();
    }
}
