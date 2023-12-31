package com.lecture.coordinator.tests;

import at.qe.memori.g7.t1.exceptions.card.DuplicateCardException;
import at.qe.memori.g7.t1.exceptions.card.MissingCardValueException;
import at.qe.memori.g7.t1.model.card.Card;
import at.qe.memori.g7.t1.model.card.Difficulty;
import at.qe.memori.g7.t1.model.card.UserCardData;
import at.qe.memori.g7.t1.model.deck.Deck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@WebAppConfiguration
public class UserCardDataServiceTest {

    @Autowired
    UserCardDataService userCardDataService;

    @Autowired
    UserDeckDataService userDeckDataService;

    @Autowired
    CardService cardService;

    @Autowired
    DeckService deckService;

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    @DisplayName("Check size")
    public void dataInitializationTest() {
        assertEquals(17, userCardDataService.size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    @DisplayName("Check if UserCardData entities get created with Card entity")
    public void checkUserCardDataCreationWithCardTest() throws DuplicateCardException, MissingCardValueException {
        Deck deck = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();
        Card card = new Card("test card test front", "test card test back", false, deck);

        // for every UserDeckData with this deck create a UserCardData with the new Card
        var newCard = cardService.createCard(card, deck);

        assertEquals(newCard.getId(), newCard.getId());
        assertEquals(newCard.getFront(), newCard.getFront());
        assertEquals(newCard.getBack(), newCard.getBack());
        // there are 2 UserDeckData for this deck so +2
        assertEquals(19, userCardDataService.size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    @DisplayName("Check if UserCardData entities get created with Card entity")
    public void checkUserCardDataCreationWithReversibleCardTest() throws DuplicateCardException, MissingCardValueException {
        Deck deck = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();
        Card card = new Card("test card test front", "test card test back", true, deck);

        // for every UserDeckData with this deck create a UserCardData with the new Card
        var newCard = cardService.createCard(card, deck);

        assertEquals(newCard.getId(), newCard.getId());
        assertEquals(newCard.getFront(), newCard.getFront());
        assertEquals(newCard.getBack(), newCard.getBack());
        // there are 2 UserDeckData for this deck so +4
        assertEquals(21, userCardDataService.size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    @DisplayName("Check if default values are set")
    public void checkDefaultValues() throws DuplicateCardException, MissingCardValueException {
        Deck deck = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();
        Card card = new Card("test card test front", "test card test back", false, deck);
        cardService.createCard(card, deck);
        Iterable<UserCardData> userCardDatas = userCardDataService.getByCard(card);

        for (UserCardData userCardData : userCardDatas) {
            assertEquals(0, userCardData.getRepetitions());
            assertEquals(Difficulty.NEW, userCardData.getDifficultyRating());
            assertEquals(2.5D, userCardData.getEFactor());
            assertEquals(0, userCardData.getLearnInterval());
            assertEquals(card, userCardData.getCard());
        }
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void checkLearnableCards() {
        Deck deck = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();
        var userDeckData = userDeckDataService.get(deck).iterator().next();

        var learnableCards = userCardDataService.getLearnableCards(userDeckData);

        Assertions.assertEquals(17, learnableCards.size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testSaveUserCardData() {
        Deck deck = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();
        var userDeckData = userDeckDataService.get(deck).iterator().next();
        var learnableCards = userCardDataService.getLearnableCards(userDeckData);

        var learnCard = learnableCards.iterator().next();

        learnCard.setLearnInterval(1);
        learnCard.setRepetitions(2);
        learnCard.setEFactor(2);

        userCardDataService.saveUserCardData(learnCard);

        var storedCardDataOpt = userCardDataService.get(learnCard.getId());

        Assertions.assertTrue(storedCardDataOpt.isPresent());

        var storedCardData = storedCardDataOpt.get();

        Assertions.assertEquals(storedCardData.getLearnInterval(), learnCard.getLearnInterval());
        Assertions.assertEquals(storedCardData.getRepetitions(), learnCard.getRepetitions());
        Assertions.assertEquals(storedCardData.getEFactor(), learnCard.getEFactor());
        Assertions.assertEquals(storedCardData.getNextDate(), learnCard.getNextDate());
    }

}
