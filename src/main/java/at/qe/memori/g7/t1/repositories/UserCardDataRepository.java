package at.qe.memori.g7.t1.repositories;

import at.qe.memori.g7.t1.model.card.Card;
import at.qe.memori.g7.t1.model.card.UserCardData;
import at.qe.memori.g7.t1.model.deck.UserDeckData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserCardDataRepository extends AbstractRepository<UserCardData, UUID> {
    Iterable<UserCardData> findByCard(Card card);

    Iterable<UserCardData> findByUserDeckData(UserDeckData userDeckData);

    @Query("SELECT ucd FROM UserCardData ucd WHERE ucd.userDeckData = :udd AND ucd.nextDate <= CURRENT_DATE ORDER BY ucd.difficultyRating ASC")
    Iterable<UserCardData> getLearnableCards(@Param("udd") UserDeckData userDeckData);
}
