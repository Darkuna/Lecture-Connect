package at.qe.memori.g7.t1.repositories;

import at.qe.memori.g7.t1.model.Userx;
import at.qe.memori.g7.t1.model.deck.Deck;
import at.qe.memori.g7.t1.model.deck.UserDeckData;

import java.util.Optional;
import java.util.UUID;

public interface UserDeckDataRepository extends AbstractRepository<UserDeckData, UUID> {
    Iterable<UserDeckData> findByUser(Userx user);

    Iterable<UserDeckData> findByDeck(Deck deck);

    Optional<UserDeckData> findByUserAndDeck(Userx user, Deck deck);
}
