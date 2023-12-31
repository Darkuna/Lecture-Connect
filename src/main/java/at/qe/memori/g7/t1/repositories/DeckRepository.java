package at.qe.memori.g7.t1.repositories;

import java.util.UUID;

import at.qe.memori.g7.t1.model.Userx;
import at.qe.memori.g7.t1.model.deck.Deck;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for managing {@link Deck} entities.
 * 
 */
public interface DeckRepository extends AbstractRepository<Deck, UUID> {

    @Query("SELECT d FROM Deck d WHERE d.owner=?1 AND d.isActive = true")
    Iterable<Deck> findByOwner(Userx owner);

    @Query("SELECT d FROM Deck d WHERE d.isPublic = true AND d.isActive = true")
    Iterable<Deck> findPublicDecks();

    @Query("SELECT d FROM Deck d WHERE d.isActive = false")
    Iterable<Deck> findDisactivatedDecks();
}
