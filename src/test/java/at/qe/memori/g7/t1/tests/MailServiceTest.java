package at.qe.memori.g7.t1.tests;

import at.qe.memori.g7.t1.model.deck.Deck;
import at.qe.memori.g7.t1.services.MailService;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
public class MailServiceTest {
    @Autowired
    private MailService mailService;

    @RegisterExtension
    static final GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("admin", "passwd"))
            .withPerMethodLifecycle(true);

    @Test
    public void sendMessageTest() throws MessagingException {
        var recipient = "testmail@testmail.com";
        var subject = "Mail Service Test";
        var body = "This is a test EMail";
        mailService.sendMail(recipient, subject, body);

        Assertions.assertEquals(1, greenMail.getReceivedMessages().length);

        var message = greenMail.getReceivedMessages()[0];
        Assertions.assertEquals(recipient, message.getRecipients(Message.RecipientType.TO)[0].toString());
        Assertions.assertEquals(subject, message.getSubject());
        Assertions.assertEquals(body, GreenMailUtil.getBody(message));
    }

    @Test
    public void sendMessageFailTest() {
        Assertions.assertThrows(MessagingException.class, () -> mailService.sendMail("not an email", "", ""));
    }

    @Test
    public void sendDeactivateMailTest() throws MessagingException {
        var recipient = "testmail@testmail.com";

        Deck deck = new Deck();
        deck.setName("Test Deck");
        mailService.sendDeactivateMail(deck, recipient);

        Assertions.assertEquals(1, greenMail.getReceivedMessages().length);
        var message = greenMail.getReceivedMessages()[0];
        Assertions.assertEquals(recipient, message.getRecipients(Message.RecipientType.TO)[0].toString());
        Assertions.assertEquals("Deck was blocked", message.getSubject());
        String expectedBody = "Dear User,%nYour deck named '%s' was blocked by an admin.%nYours,%nThe EduCards Team :)"
                .formatted(deck.getName()).replaceAll("(\\r\\n|\\r|\\n)", "\r\n");
        Assertions.assertEquals(expectedBody, GreenMailUtil.getBody(message));
    }

}
