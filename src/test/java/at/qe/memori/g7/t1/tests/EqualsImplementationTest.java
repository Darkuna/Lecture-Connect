package at.qe.memori.g7.t1.tests;

import at.qe.memori.g7.t1.model.Userx;
import at.qe.memori.g7.t1.model.UserxRole;
import at.qe.memori.g7.t1.model.card.Card;
import at.qe.memori.g7.t1.model.card.UserCardData;
import at.qe.memori.g7.t1.model.deck.Deck;
import at.qe.memori.g7.t1.model.deck.UserDeckData;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 * Tests to ensure that each entity's implementation of equals conforms to the
 * contract. See {@linkplain http://www.jqno.nl/equalsverifier/} for more
 * information.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
public class EqualsImplementationTest {

    @Test
    public void testUserEqualsContract() {
        Userx user1 = new Userx();
        user1.setUsername("user1");
        Userx user2 = new Userx();
        user2.setUsername("user2");
        Deck deck1 = new Deck();
        deck1.setCards(new ArrayList<>());
        Deck deck2 = new Deck();
        deck2.setCards(new ArrayList<>());
        Card card1 = new Card();
        Card card2 = new Card();
        UserDeckData deckData1 = new UserDeckData(user1, deck1);
        UserDeckData deckData2 = new UserDeckData(user2, deck2);
        UserCardData cardData1 = new UserCardData(deckData1, card1, false);
        UserCardData cardData2 = new UserCardData(deckData2, card2, false);
        EqualsVerifier.forClass(Userx.class)
                .withPrefabValues(Card.class, card1, card2)
                .withPrefabValues(Deck.class, deck1, deck2)
                .withPrefabValues(UserDeckData.class, deckData1, deckData2)
                .withPrefabValues(UserCardData.class, cardData1, cardData2)
                .withPrefabValues(Userx.class, user1, user2)
                .suppress(Warning.STRICT_INHERITANCE, Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
    }

    @Test
    public void testUserRoleEqualsContract() {
        EqualsVerifier.forClass(UserxRole.class).verify();
    }

}