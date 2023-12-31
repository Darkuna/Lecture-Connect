package at.qe.memori.g7.t1.services;

import at.qe.memori.g7.t1.exceptions.deck.DuplicateDeckException;
import at.qe.memori.g7.t1.exceptions.deck.MissingDescritionException;
import at.qe.memori.g7.t1.exceptions.deck.MissingNameException;
import at.qe.memori.g7.t1.exceptions.deck.NoSuchDeckException;
import at.qe.memori.g7.t1.model.Userx;
import at.qe.memori.g7.t1.model.deck.Deck;
import at.qe.memori.g7.t1.model.deck.UserDeckData;
import at.qe.memori.g7.t1.repositories.DeckRepository;
import at.qe.memori.g7.t1.repositories.UserDeckDataRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Service for accessing and manipulating decks.
 */
@Component
@Scope("application")
public class DeckService {

    private final DeckRepository deckRepository;

    private final UserDeckDataRepository userDeckDataRepository;


    public DeckService(DeckRepository deckRepository, UserDeckDataRepository userDeckDataRepository) {
        this.deckRepository = deckRepository;
        this.userDeckDataRepository = userDeckDataRepository;
    }

    /**
     * overrides the isActive attribute with true
     *
     * @return
     * @throws NoSuchDeckException
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Iterable<Deck> getAllDecks() {
        return deckRepository.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Iterable<Deck> getDisactivatedDecks() {
        return deckRepository.findDisactivatedDecks();
    }

    /**
     * overrides the isActive attribute with true
     *
     * @param deck
     * @return
     * @throws NoSuchDeckException
     */
    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #deck.owner.username")
    public Deck activateDeck(Deck deck) throws NoSuchDeckException {
        deck.setActive(true);

        return updateDeck(deck);
    }

    /**
     * overrides the isActive attribute with false
     *
     * @param deck
     * @return
     * @throws NoSuchDeckException
     */
    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #deck.owner.username")
    public Deck deactivateDeck(Deck deck) throws NoSuchDeckException {
        deck.setActive(false);
        return updateDeck(deck);
    }

    /**
     * overrides the isPublic attribute with true
     *
     * @param deck
     * @return
     * @throws NoSuchDeckException
     */
    @PreAuthorize("principal.username eq #deck.owner.username")
    public Deck publishDeck(Deck deck) throws NoSuchDeckException {
        deck.setPublic(true);

        return updateDeck(deck);
    }

    /**
     * overrides the isPublic attribute with false
     *
     * @param deck
     * @return
     * @throws NoSuchDeckException
     */
    @PreAuthorize("principal.username eq #deck.owner.username")
    public Deck unpublishDeck(Deck deck) throws NoSuchDeckException {
        deck.setPublic(false);

        return updateDeck(deck);
    }

    /**
     * overrides the isPublic attribute with false
     *
     * @param deck
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #deck.owner.username")
    public void deleteDeck(Deck deck) {
        deckRepository.delete(deck);
    }

    /**
     * checks if the currently authenticated user is the owner of the deck
     *
     * @param user
     * @return
     */
    public Iterable<Deck> getOwnedDecks(Userx user) {
        return deckRepository.findByOwner(user);
    }

    /**
     * method to update a deck
     * can be used to update a deck
     *
     * @param deck
     * @return
     * @throws NoSuchDeckException
     */
    @PreAuthorize("principal.username eq #deck.owner.username")
    public Deck updateDeck(Deck deck) throws NoSuchDeckException {
        if (deckRepository.findById(deck.getDeckID()).isEmpty()) {
            throw new NoSuchDeckException("Cannot update deck, deck with such ID does not exists");
        }

        return saveDeck(deck);
    }

    private void checkDeckRequirements(Deck deck) throws MissingNameException, MissingDescritionException {
        if (deck.getName() == null || deck.getName().isBlank()) {
            throw new MissingNameException("Titel des Decks fehlt");
        }

        if (deck.getDescription() == null || deck.getDescription().isBlank()) {
            throw new MissingDescritionException("Deck Beschreibung fehlt");
        }
    }

    /**
     * method to save a deck
     * can be used to update or create a deck
     *
     * @param deck
     * @return
     * @throws DuplicateDeckException
     */
    public Deck createDeck(Deck deck, Userx currentUser)
            throws DuplicateDeckException, MissingNameException, MissingDescritionException {
        if (deckRepository.findById(deck.getDeckID()).isPresent()) {
            throw new DuplicateDeckException("Cannot create deck, deck with such ID already exists");
        }

        checkDeckRequirements(deck);

        deck.setOwner(currentUser);
        Deck newDeck = saveDeck(deck);
        userDeckDataRepository.save(new UserDeckData(currentUser, deck));
        return newDeck;
    }

    /**
     * method to save a deck
     * can be used to update or create a deck
     *
     * @param deck
     * @return
     */
    private Deck saveDeck(Deck deck) {
        return deckRepository.save(deck);
    }

    public Optional<Deck> loadDeck(UUID id) {
        return deckRepository.findById(id);
    }

    /**
     * get all decks that are public
     *
     * @return
     */
    public Iterable<Deck> getPublicDecks() {
        return deckRepository.findPublicDecks();
    }
}
