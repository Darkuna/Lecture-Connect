package at.qe.memori.g7.t1.ui.controllers;

import at.qe.memori.g7.t1.exceptions.deck.NoSuchDeckException;
import at.qe.memori.g7.t1.exceptions.report.DuplicateReportException;
import at.qe.memori.g7.t1.model.Report;
import at.qe.memori.g7.t1.model.ReportState;
import at.qe.memori.g7.t1.model.Userx;
import at.qe.memori.g7.t1.model.UserxRole;
import at.qe.memori.g7.t1.model.card.Card;
import at.qe.memori.g7.t1.model.deck.Deck;
import at.qe.memori.g7.t1.services.*;
import at.qe.memori.g7.t1.ui.beans.SessionInfoBean;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@Scope("view")
public class ReportDeckController {
    @Autowired
    protected SessionInfoBean sessionInfoBean;

    @Autowired
    protected DeckService deckService;

    @Autowired
    protected UserDeckDataService userDeckDataService;

    @Autowired
    MailService mailService;

    @Autowired
    protected CardService cardService;

    @Autowired
    protected ReportService reportService;

    private List<Report> reports;

    @PostConstruct
    public void init() {
        if (sessionInfoBean.getCurrentUser().getRoles().contains(UserxRole.ADMIN)) {
            this.reports = StreamSupport.stream(reportService.getAllReports().spliterator(), false)
                    .collect(Collectors.toList());
        }
    }

    public void createReport() {
        Report report = new Report();
        report.setReason("Reported from a User");
        report.setState(ReportState.REPORTED);
        report.setDeck(sessionInfoBean.getDeck());
        report.setReporter(sessionInfoBean.getCurrentUser());

        try {
            reportService.createReport(report, sessionInfoBean.getCurrentUser());
        } catch (DuplicateReportException e) {
            ControllerUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Duplicate report", "You already reported this deck");
        }
    }

    public void acceptReport(Report report) {
        this.deactivateDeck(report.getDeck());
        reportService.deleteUserReport(report);
        init();
    }

    public void denyReport(Report report) {
        reportService.deleteUserReport(report);
        init();
    }

    public Iterable<Card> getCardsOfDeck(Deck deck) {
        return cardService.get(deck);
    }

    public Iterable<Deck> getDisactivatedDecks() {
        return deckService.getDisactivatedDecks();
    }

    public void activateDeck(Deck deck) {
        try {
            deckService.activateDeck(deck);
        } catch (NoSuchDeckException e) {
            ControllerUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Deck does not exist", "How did you do that");
        }
    }

    public void deactivateDeck(Deck deck) {
        try {
            deckService.deactivateDeck(deck);
        } catch (NoSuchDeckException e) {
            ControllerUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Deck does not exist", "How did you do that");
        }
        sendReportMail(deck, deck.getOwner());

        for (var deckData : userDeckDataService.get(deck)) {
            if (!deck.getOwner().equals(deckData.getUser())) {
                sendReportMail(deck, deckData.getUser());
            }
        }
    }

    private void sendReportMail(Deck deck, Userx user) {
        try {
            mailService.sendDeactivateMail(deck, user.getEmail());
        } catch (MessagingException e) {
            ControllerUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Mail could not be sent", "There was a problem sending the mail to the user");
        }
        ControllerUtils.addMessage(FacesMessage.SEVERITY_INFO, "Email was send to user", "Email send to: " + user.getEmail());
    }

    public boolean checkIfUserReportedDeck() {
        Iterable<Report> reportsByUser = reportService.findByReporter(sessionInfoBean.getCurrentUser(), sessionInfoBean.getDeck());

        if (reportsByUser == null) {
            return false;
        } else {
            return StreamSupport.stream(reportsByUser.spliterator(), false).anyMatch(report -> sessionInfoBean.getCurrentUser().equals(report.getReporter()));
        }
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;

    }
}

