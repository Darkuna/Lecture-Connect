package at.qe.memori.g7.t1.ui.beans;

import at.qe.memori.g7.t1.model.deck.Deck;
import at.qe.memori.g7.t1.services.DeckService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;


/**
 * Session information bean to retrieve session-specific parameters.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Component
@Scope("view")
public class ShowDecksBean {

    private final DeckService deckService;

    private final SessionInfoBean sessionInfoBean;

    private boolean togglePublicDecks;
    private Deck deck;
    private Iterable<Deck> privateDecks;
    private final Integer numberOfPrivateDecksShown;

    private Iterable<Deck> publicDecks;
    private final Integer numberOfPublicDecksShown;

    public ShowDecksBean(DeckService deckService, SessionInfoBean sessionInfoBean) {
        this.deckService = deckService;
        this.sessionInfoBean = sessionInfoBean;

        // initialize rest with beans
        this.togglePublicDecks = false;
        loadDecks();
        this.numberOfPrivateDecksShown = calculateDecksShown(privateDecks);
        this.numberOfPublicDecksShown = calculateDecksShown(publicDecks);
    }

    public Integer calculateDecksShown(Iterable<Deck> deckList) {
        if (deckList == null) {
            return 1;
        } else {
            long size = StreamSupport.stream(deckList.spliterator(), false).count();
            return complicatedFormula(size);
        }
    }

    public void loadDecks() {
        publicDecks = deckService.getPublicDecks();
        privateDecks = deckService.getOwnedDecks(sessionInfoBean.getCurrentUser());
    }

    public Iterable<Deck> getDecks() {
        loadDecks();
        if (this.togglePublicDecks) {
            return publicDecks;
        }
        return privateDecks;
    }

    public boolean isTogglePublicDecks() {
        return togglePublicDecks;
    }

    public void setTogglePublicDecks(boolean showPublicDecks) {
        this.togglePublicDecks = showPublicDecks;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Integer getNumberOfPrivateDecksShown() {
        return numberOfPrivateDecksShown;
    }

    public Integer getNumberOfPublicDecksShown() {
        return numberOfPublicDecksShown;
    }

    private Integer complicatedFormula(long length) {
        if (length > 6) {
            return 3;
        } else if (length > 3) {
            return 2;
        }
        return 1;
    }
}
