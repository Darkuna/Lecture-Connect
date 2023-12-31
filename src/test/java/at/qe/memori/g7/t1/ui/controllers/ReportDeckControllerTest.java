package at.qe.memori.g7.t1.ui.controllers;

import at.qe.memori.g7.t1.model.Report;

import at.qe.memori.g7.t1.repositories.ReportRepository;
import at.qe.memori.g7.t1.services.DeckService;
import at.qe.memori.g7.t1.services.ReportService;
import at.qe.memori.g7.t1.services.UserService;
import at.qe.memori.g7.t1.ui.beans.SessionInfoBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;


@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
class ReportDeckControllerTest {

    @Autowired
    SessionInfoBean sessionInfoBean;

    @Autowired
    ReportService reportService;

    @Autowired
    ReportRepository reportRepository;

    @MockBean
    ReportDeckController reportDeckController;

    @Autowired
    DeckService deckService;

    @Autowired
    UserService userService;


    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void createReport() {
        var user = userService.loadUser("ADMIN").get();
        var deck = deckService.loadDeck(UUID.fromString("9ec2dda8-3850-4ffd-bc25-fabc732adaec")).get();

        sessionInfoBean.setDeck(deck);
        reportDeckController.createReport();

        //Assertions.assertEquals(4, reportDeckController.getReports().size());
        Assertions.assertEquals(4, reportService.size());
    }

    @Test
    void activateDeck() {
    }

    @Test
    void deactivateDeck() {
    }

    @Test
    void checkIfUserReportedDeck() {
    }

    @Test
    @WithMockUser(username = "user1", authorities = "USER")
    void getReportsAsUser() {
        reportDeckController.init();
        Assertions.assertEquals(0, reportDeckController.getReports().size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void getReports() {
        reportDeckController.init();
        Assertions.assertEquals(3, reportDeckController.getReports().size());
    }
}