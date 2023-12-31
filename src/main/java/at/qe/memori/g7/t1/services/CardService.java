package at.qe.memori.g7.t1.services;

import at.qe.memori.g7.t1.exceptions.card.DuplicateCardException;
import at.qe.memori.g7.t1.exceptions.card.MissingCardValueException;
import at.qe.memori.g7.t1.exceptions.card.NoSuchCardException;
import at.qe.memori.g7.t1.model.card.Card;
import at.qe.memori.g7.t1.model.card.UserCardData;
import at.qe.memori.g7.t1.model.deck.Deck;
import at.qe.memori.g7.t1.model.deck.UserDeckData;
import at.qe.memori.g7.t1.repositories.CardRepository;
import at.qe.memori.g7.t1.repositories.UserCardDataRepository;
import at.qe.memori.g7.t1.repositories.UserDeckDataRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Service for accessing and manipulating user data.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Component
@Scope("application")
public class CardService {

    private final CardRepository cardRepository;

    private final UserCardDataRepository userCardDataRepository;

    private final UserDeckDataRepository userDeckDataRepository;

    public CardService(CardRepository cardRepository, UserCardDataRepository userCardDataRepository, UserDeckDataRepository userDeckDataRepository) {
        this.cardRepository = cardRepository;
        this.userCardDataRepository = userCardDataRepository;
        this.userDeckDataRepository = userDeckDataRepository;
    }

    /**
     * Returns an iterable of all cards.
     *
     * @return Iterable<Card>
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Iterable<Card> get() {
        return cardRepository.findAll();
    }

    /**
     * Returns an iterable of all cards in a deck
     *
     * @return Iterable<Card>
     */
    @PreAuthorize("hasAuthority('ADMIN') or #deck.isPublic() or principal.username eq #deck.owner.username")
    public Iterable<Card> get(Deck deck) {
        return cardRepository.findByDeckIncluded(deck);
    }

    private void checkCardRequirements(Card card) throws MissingCardValueException {
        if (card.getFront() == null || card.getFront().isBlank()) {
            throw new MissingCardValueException("Question cannot be empty");
        }

        if (card.getBack() == null || card.getBack().isBlank()) {
            throw new MissingCardValueException("Answer cannot be empty");
        }
    }

    /**
     * create a card
     *
     * @return Iterable<Card>
     * @throws DuplicateCardException
     * @throws MissingCardValueException
     */
    public Card createCard(Card card, Deck deckIncluded) throws DuplicateCardException, MissingCardValueException {
        if (card.getId() == null) {
            card.setCardID(UUID.randomUUID());
        }
        if (cardRepository.findById(card.getId()).isPresent()) {
            throw new DuplicateCardException("Cannot create card, card with such ID already exists");
        }
        card.setDeckIncluded(deckIncluded);
        checkCardRequirements(card);
        cardRepository.save(card);

        for (UserDeckData deckData : userDeckDataRepository.findByDeck(deckIncluded)) {
            userCardDataRepository.save(new UserCardData(deckData, card, false));

            if (card.isReversible()) {
                userCardDataRepository.save(new UserCardData(deckData, card, true));
            }
        }

        return card;
    }

    /**
     * update a card
     *
     * @return Card
     * @throws NoSuchCardException
     */
    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #card.deckIncluded.owner.username")
    public Card updateCard(Card card) throws NoSuchCardException {
        if (card.getId() == null) {
            throw new NoSuchCardException("Cannot update card, ID is null");
        }
        if (cardRepository.findById(card.getId()).isEmpty()) {
            throw new NoSuchCardException("Cannot update card, card with such ID does not exists");
        }

        return cardRepository.save(card);
    }

    /**
     * delete a card
     */
    public void deleteCard(Card card) {
        cardRepository.delete(card);
    }

    public long size() {
        return cardRepository.count();
    }

    /**
     * Loads a single card with UUID id.
     *
     * @return the user with the given username
     */
    public Optional<Card> loadCard(UUID id) {
        return cardRepository.findById(id);
    }
}
