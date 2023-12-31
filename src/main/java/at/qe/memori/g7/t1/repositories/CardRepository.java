package at.qe.memori.g7.t1.repositories;

import at.qe.memori.g7.t1.model.card.Card;
import at.qe.memori.g7.t1.model.deck.Deck;

import java.util.UUID;

/**
 * Repository for managing {@link Card} entities.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
public interface CardRepository extends AbstractRepository<Card, UUID> {
    Iterable<Card> findByDeckIncluded(Deck deck);
}
