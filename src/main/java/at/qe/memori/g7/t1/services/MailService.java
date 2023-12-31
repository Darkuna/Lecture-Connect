package at.qe.memori.g7.t1.services;

import at.qe.memori.g7.t1.model.deck.Deck;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@Scope("application")
public class MailService {
    private final String email;
    private final String appPassword;

    private final Session session;

    public MailService(Environment env) {
        email = env.getProperty("mail.username");
        appPassword = env.getProperty("mail.password");

        Properties properties = new Properties();
        properties.put("mail.smtp.host", env.getProperty("mail.smtp.host"));
        properties.put("mail.smtp.port", env.getProperty("mail.smtp.port"));
        properties.put("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
        properties.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable")); // TLS


        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, appPassword);
            }
        });
    }

    public void sendMail(final String recipient, final String subject, final String body) throws MessagingException {
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(email));

        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }

    public void sendDeactivateMail(Deck deck, String address) throws MessagingException {
        var subject = "Deck was blocked";
        var body = "Dear User,%nYour deck named '%s' was blocked by an admin.%nYours,%nThe EduCards Team :)"
                .formatted(deck.getName());

        this.sendMail(address, subject, body);
    }
}
