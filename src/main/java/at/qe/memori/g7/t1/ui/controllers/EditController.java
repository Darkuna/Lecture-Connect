package at.qe.memori.g7.t1.ui.controllers;

import at.qe.memori.g7.t1.exceptions.card.DuplicateCardException;
import at.qe.memori.g7.t1.exceptions.card.MissingCardValueException;
import at.qe.memori.g7.t1.exceptions.card.NoSuchCardException;
import at.qe.memori.g7.t1.exceptions.deck.NoSuchDeckException;
import at.qe.memori.g7.t1.model.card.Card;
import at.qe.memori.g7.t1.model.deck.Deck;
import at.qe.memori.g7.t1.services.CardService;
import at.qe.memori.g7.t1.services.DeckService;
import at.qe.memori.g7.t1.ui.beans.SessionInfoBean;
import org.primefaces.event.RowEditEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;

@Component
@Scope("view")
public class EditController {

    private final CardService cardService;

    private final DeckService deckService;

    private final SessionInfoBean sessionInfoBean;
    
    private Card card;
    private Iterable<Card> cards;

    private final Deck deck;

    private boolean editDeck = false;


    public EditController(CardService cardService, DeckService deckService, SessionInfoBean sessionInfoBean) {
        this.cardService = cardService;
        this.deckService = deckService;
        this.sessionInfoBean = sessionInfoBean;

        deck = sessionInfoBean.getDeck();

        updateCards();
    }

    private void updateCards() {
        card = new Card();
        cards = cardService.get(deck);
    }

    public void enterDeckEdit() {
        editDeck = true;
    }

    public void saveDeck() {
        try {
            deckService.updateDeck(sessionInfoBean.getDeck());
            editDeck = false;
        } catch (NoSuchDeckException e) {
            ControllerUtils.addMessage(FacesMessage.SEVERITY_FATAL, "Update Deck", "ERROR updating deck: unknown error");
        }
    }

    public void exitDeckEdit() {
        editDeck = false;
    }

    public void createCard() {
        try {
            this.cardService.createCard(this.card, deck);
            updateCards();
        } catch (DuplicateCardException e) {
            ControllerUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Create Card", "Card already exists");
        } catch (MissingCardValueException e) {
            ControllerUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Create Card", e.getMessage());
        }
    }

    public void updateCardConfirm(RowEditEvent<Card> event) {
        try {
            this.cardService.updateCard(event.getObject());
        } catch (NoSuchCardException e) {
            ControllerUtils.addMessage(FacesMessage.SEVERITY_FATAL, "Card", "ERROR updating card: unknown error");
        }
        ControllerUtils.addMessage(FacesMessage.SEVERITY_INFO, "Card Edited", event.getObject().getFront());
    }

    public void updateCardCancel(RowEditEvent<Card> event) {
        ControllerUtils.addMessage(FacesMessage.SEVERITY_INFO, "Card Edit Canceled", event.getObject().getFront());
    }

    public void clearCard() {
        card = new Card();
    }

    public void deleteCard(Card cardToDelete) {
        cardService.deleteCard(cardToDelete);
        updateCards();
    }

    public void goBack() {
        sessionInfoBean.setDeck(null);

        ControllerUtils.redirect("welcome.xhtml");
    }

    public Iterable<Card> getCards() {
        return cards;
    }

    public Card getCard() {
        return card;
    }

    public boolean isEditDeck() {
        return editDeck;
    }

    public Deck getDeck() {
        return deck;
    }
}
