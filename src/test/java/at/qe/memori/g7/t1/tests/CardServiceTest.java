package at.qe.memori.g7.t1.tests;

import at.qe.memori.g7.t1.exceptions.card.DuplicateCardException;
import at.qe.memori.g7.t1.exceptions.card.MissingCardValueException;
import at.qe.memori.g7.t1.exceptions.card.NoSuchCardException;
import at.qe.memori.g7.t1.model.card.Card;
import at.qe.memori.g7.t1.model.deck.Deck;
import at.qe.memori.g7.t1.services.CardService;
import at.qe.memori.g7.t1.services.DeckService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@WebAppConfiguration
public class CardServiceTest {

    @Autowired
    private CardService cardService;

    @Autowired
    private DeckService deckService;

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testGetAllCards() {
        int amountCards = 17;
        Assertions.assertEquals(amountCards, cardService.size());

        Iterable<Card> cards = cardService.get();

        Assertions.assertEquals(amountCards, Lists.newArrayList(cards).size());

        List<UUID> cardIds = new ArrayList<>();
        cardIds.add(UUID.fromString("291771c2-b76f-411a-b54d-cd9cd0c27f9f"));
        cardIds.add(UUID.fromString("130fa75a-33ed-417f-bdc5-e4e34482ad20"));
        cardIds.add(UUID.fromString("21fbafe5-ad3d-47da-9b13-9ab2d5d290b0"));
        cardIds.add(UUID.fromString("70811d0f-061b-425c-82d9-0d8c7209ee9a"));
        cardIds.add(UUID.fromString("3264bc82-0b4a-4578-82c8-22ba6d9c1126"));
        cardIds.add(UUID.fromString("07f39c5f-cfc8-4397-9668-e129e9bec267"));
        cardIds.add(UUID.fromString("61c49c72-de90-4523-9388-96b3e09e52d0"));
        cardIds.add(UUID.fromString("4d9c0fc3-cba6-4b2d-a976-7a4f400154bd"));
        cardIds.add(UUID.fromString("23a92b3d-2ebe-445b-984e-000acd711689"));
        cardIds.add(UUID.fromString("cb79d4ae-f853-439e-b62a-77b834103ee6"));
        cardIds.add(UUID.fromString("93806103-8575-4142-89af-f6b99142ce59"));
        cardIds.add(UUID.fromString("ccaa1a7e-49ab-4d9a-9ffc-28c6f5403b32"));
        cardIds.add(UUID.fromString("439371b7-5850-4da6-bf28-c89cdd93049c"));
        cardIds.add(UUID.fromString("e28b9fbb-22f7-454e-bee7-a6f9174479af"));
        cardIds.add(UUID.fromString("acd39f43-fe2c-4e05-b2a6-92fe143c0df2"));
        cardIds.add(UUID.fromString("78cdd252-65ac-473c-89f0-905032670257"));
        cardIds.add(UUID.fromString("9590d3bf-d514-4946-8942-04c46b27756f"));

        for (Card card : cards) {
            Assertions.assertTrue(cardIds.contains(card.getId()));
        }
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testGetAllCardsInDeck() {
        Deck deck = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();

        Iterable<Card> cards = cardService.get(deck);

        Assertions.assertEquals(4, Lists.newArrayList(cards).size());

        List<UUID> cardIds = new ArrayList<>();
        cardIds.add(UUID.fromString("291771c2-b76f-411a-b54d-cd9cd0c27f9f"));
        cardIds.add(UUID.fromString("21fbafe5-ad3d-47da-9b13-9ab2d5d290b0"));
        cardIds.add(UUID.fromString("70811d0f-061b-425c-82d9-0d8c7209ee9a"));
        cardIds.add(UUID.fromString("3264bc82-0b4a-4578-82c8-22ba6d9c1126"));

        for (Card card : cards) {
            Assertions.assertTrue(cardIds.contains(card.getId()));
        }
    }

    @Test
    public void testGetCard() {
        UUID id = UUID.fromString("291771c2-b76f-411a-b54d-cd9cd0c27f9f");

        Optional<Card> card = cardService.loadCard(id);
        if (card.isEmpty()) {
            Assertions.fail();
        }

        Card actualCard = card.get();

        Assertions.assertEquals(UUID.fromString("291771c2-b76f-411a-b54d-cd9cd0c27f9f"), actualCard.getId());
        Assertions.assertEquals("front test 1", actualCard.getFront());
        Assertions.assertEquals("back test 1", actualCard.getBack());
        Assertions.assertEquals(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36"),
                actualCard.getDeckIncluded().getId());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testDeleteCard() {
        UUID id = UUID.fromString("291771c2-b76f-411a-b54d-cd9cd0c27f9f");

        Optional<Card> card = cardService.loadCard(id);
        if (card.isEmpty()) {
            Assertions.fail();
        }

        Card actualCard = card.get();

        cardService.deleteCard(actualCard);

        List<Card> cards = Lists.newArrayList(cardService.get());
        Assertions.assertEquals(16, cards.size());

        Assertions.assertFalse(cards.contains(actualCard));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testCreateCard() {
        Deck deck = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();

        Card card = new Card(
                "test card test front",
                "test card test back",
                false,
                deck);

        try {
            cardService.createCard(card, deck);
        } catch (Exception e) {
            Assertions.fail();
        }

        List<Card> cards = Lists.newArrayList(cardService.get());
        Assertions.assertEquals(18, cards.size());

        Assertions.assertTrue(cards.contains(card));

        Optional<Card> newCard = cardService.loadCard(card.getId());

        if (newCard.isEmpty()) {
            Assertions.fail();
        }

        Card actualNewCard = newCard.get();
        Assertions.assertEquals(card.getId(), actualNewCard.getId());
        Assertions.assertEquals(card.getFront(), actualNewCard.getFront());
        Assertions.assertEquals(card.getBack(), actualNewCard.getBack());
        Assertions.assertEquals(card.getDeckIncluded(), actualNewCard.getDeckIncluded());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testCreateCard_cardIdNull() {
        Deck deck = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();

        Card card = new Card(
                "test card test front",
                "test card test back",
                false,
                deck);

        card.setCardID(null);

        try {
            cardService.createCard(card, deck);
        } catch (Exception e) {
            Assertions.fail();
        }

        List<Card> cards = Lists.newArrayList(cardService.get());
        Assertions.assertEquals(18, cards.size());

        Assertions.assertTrue(cards.contains(card));

        Optional<Card> newCard = cardService.loadCard(card.getId());

        if (newCard.isEmpty()) {
            Assertions.fail();
        }

        Card actualNewCard = newCard.get();
        Assertions.assertEquals(card.getId(), actualNewCard.getId());
        Assertions.assertEquals(card.getFront(), actualNewCard.getFront());
        Assertions.assertEquals(card.getBack(), actualNewCard.getBack());
        Assertions.assertEquals(card.getDeckIncluded(), actualNewCard.getDeckIncluded());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testCreateCardThrowDuplicateException() {
        UUID id = UUID.fromString("291771c2-b76f-411a-b54d-cd9cd0c27f9f");

        Optional<Card> card = cardService.loadCard(id);
        if (card.isEmpty()) {
            Assertions.fail();
        }

        Card actualCard = card.get();

        Assertions.assertThrows(DuplicateCardException.class, () -> {
            cardService.createCard(actualCard, new Deck());
        });
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testCreateCardThrowMissingValueException_Question() {
        Card card = new Card();
        card.setBack("answer");

        Assertions.assertThrows(MissingCardValueException.class, () -> {
            cardService.createCard(card, new Deck());
        });
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testCreateCardThrowMissingValueException_Question2() {
        Card card = new Card();
        card.setFront("");
        card.setBack("answer");

        Assertions.assertThrows(MissingCardValueException.class, () -> {
            cardService.createCard(card, new Deck());
        });
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testCreateCardThrowMissingValueException_Answer() {
        Card card = new Card();
        card.setFront("question");

        Assertions.assertThrows(MissingCardValueException.class, () -> {
            cardService.createCard(card, new Deck());
        });
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testCreateCardThrowMissingValueException_Answer2() {
        Card card = new Card();
        card.setFront("question");
        card.setBack("");

        Assertions.assertThrows(MissingCardValueException.class, () -> {
            cardService.createCard(card, new Deck());
        });
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testUpdateCard() {
        UUID id = UUID.fromString("291771c2-b76f-411a-b54d-cd9cd0c27f9f");

        Optional<Card> card = cardService.loadCard(id);
        if (card.isEmpty()) {
            Assertions.fail();
        }

        Card actualCard = card.get();

        String newBack = "new back updated";
        actualCard.setBack(newBack);

        try {
            cardService.updateCard(actualCard);
        } catch (NoSuchCardException e) {
            Assertions.fail();
        }

        List<Card> cards = Lists.newArrayList(cardService.get());
        Assertions.assertEquals(17, cards.size());

        Assertions.assertTrue(cards.contains(actualCard));

        Optional<Card> updatedCard = cardService.loadCard(id);
        if (card.isEmpty()) {
            Assertions.fail();
        }

        Card actualUpdatedCard = updatedCard.get();

        Assertions.assertEquals(newBack, actualUpdatedCard.getBack());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testUpdateCardThrowNoCardException() {
        Card card = new Card();

        Assertions.assertThrows(NoSuchCardException.class, () -> {
            cardService.updateCard(card);
        });
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testUpdateCardThrowNoCardException_cardIdNull() {
        Card card = new Card();
        card.setCardID(null);

        Assertions.assertThrows(NoSuchCardException.class, () -> {
            cardService.updateCard(card);
        });
    }
}
