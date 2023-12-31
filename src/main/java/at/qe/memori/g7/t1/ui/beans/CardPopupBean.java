package at.qe.memori.g7.t1.ui.beans;

import at.qe.memori.g7.t1.model.card.Card;
import at.qe.memori.g7.t1.model.deck.Deck;
import at.qe.memori.g7.t1.services.CardService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Session information bean to retrieve session-specific parameters.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Component
@Scope("session")
public class CardPopupBean {

    private final CardService cardService;

    public CardPopupBean(CardService cardService) {
        this.cardService = cardService;
    }

    private Deck deck;

    private Card card;
    /**
     * deck wird temporär gebraucht.
     * SelectOneListBox speichert die einzelnen Decks,
     * gibt dann ihre Informationen an und anschließend wird das nächste Deck
     * geladen.
     */
    private List<Card> cards;

    private Boolean showPopup = false;


    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;

        this.showPopup = true;

        this.cards = StreamSupport.stream(cardService.get(deck).spliterator(), false)
                .collect(Collectors.toList());
    }

    public void hidePopUp() {
        this.showPopup = false;
    }

    public Boolean getShowPopup() {
        return showPopup;
    }

    public void setShowPopup(Boolean showPopup) {
        this.showPopup = showPopup;
    }
}
