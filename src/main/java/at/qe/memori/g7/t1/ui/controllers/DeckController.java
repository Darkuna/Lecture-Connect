package at.qe.memori.g7.t1.ui.controllers;

import at.qe.memori.g7.t1.exceptions.deck.DuplicateDeckException;
import at.qe.memori.g7.t1.exceptions.deck.MissingDescritionException;
import at.qe.memori.g7.t1.exceptions.deck.MissingNameException;
import at.qe.memori.g7.t1.model.deck.Deck;
import at.qe.memori.g7.t1.services.DeckService;
import at.qe.memori.g7.t1.ui.beans.SessionInfoBean;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;

@Component
@Scope("view")
public class DeckController {
    private final SessionInfoBean sessionInfoBean;

    private final DeckService deckService;

    public DeckController(SessionInfoBean sessionInfoBean, DeckService deckService) {
        this.sessionInfoBean = sessionInfoBean;
        this.deckService = deckService;
    }

    public void deleteDeck(Deck deck) {
        deckService.deleteDeck(deck);
    }

    private Deck deck;

    public void createDeck() {
        deck = new Deck();
    }

    public boolean doSaveDeck() {
        try {
            deckService.createDeck(deck, sessionInfoBean.getCurrentUser());
        } catch (DuplicateDeckException e) {
            ControllerUtils.addMessage(FacesMessage.SEVERITY_FATAL, "Duplicate", "This deck already exists!");
            return false;
        } catch (MissingNameException e) {
            ControllerUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Name missing", "You have to give the deck a name!");
            return false;
        } catch (MissingDescritionException e) {
            ControllerUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Description missing", "You have to give the deck a description!");
            return false;
        }
        // close dialog if successful
        PrimeFaces.current().executeScript("PF('createDeckDialog').hide()");
        return true;
    }

    public void doSaveAndEditDeck() {
        if (doSaveDeck()) {
            sessionInfoBean.setDeck(deck);
            ControllerUtils.redirect("editor.xhtml");
        }
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void editDeck(Deck deck) {
        sessionInfoBean.setDeck(deck);

        ControllerUtils.redirect("editor.xhtml");
    }
}
