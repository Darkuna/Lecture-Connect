package at.qe.memori.g7.t1.tests;

import at.qe.memori.g7.t1.exceptions.deck.DuplicateDeckException;
import at.qe.memori.g7.t1.exceptions.deck.MissingDescritionException;
import at.qe.memori.g7.t1.exceptions.deck.MissingNameException;
import at.qe.memori.g7.t1.exceptions.deck.NoSuchDeckException;
import at.qe.memori.g7.t1.model.deck.Deck;
import at.qe.memori.g7.t1.services.DeckService;
import at.qe.memori.g7.t1.services.UserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;

/**
 * Some very basic tests for {@link UserService}.
 */
@SpringBootTest
@WebAppConfiguration
public class DeckServiceTest {

    @Autowired
    DeckService deckService;

    @Autowired
    UserService userService;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDatainitializationDeck() {
        Assertions.assertEquals(5, Lists.newArrayList(deckService.getAllDecks()).size(),
                "Insufficient amount of decks initialized for test data source");

        Deck deck1 = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();
        Deck deck2 = deckService.loadDeck(UUID.fromString("cb5e5b13-fd0e-4be0-a29e-7d26800e94bb")).get();
        Deck deck3 = deckService.loadDeck(UUID.fromString("9ec2dda8-3850-4ffd-bc25-fabc732adaec")).get();
        Deck deck4 = deckService.loadDeck(UUID.fromString("19534ec0-bf13-4317-bb3d-91c29603b06c")).get();
        Deck deck5 = deckService.loadDeck(UUID.fromString("4af32892-5e68-49be-9c7c-84fa54140880")).get();

        Assertions.assertEquals("user1",
                deck1.getOwner().getUsername(),
                "Not the right owner initialized for deck");

        Assertions.assertEquals("user1",
                deck2.getOwner().getUsername(),
                "Not the right owner initialized for deck");

        Assertions.assertEquals("user1",
                deck3.getOwner().getUsername(),
                "Not the right owner initialized for deck");

        Assertions.assertEquals("user2",
                deck4.getOwner().getUsername(),
                "Not the right owner initialized for deck");

        Assertions.assertEquals("user2",
                deck5.getOwner().getUsername(),
                "Not the right owner initialized for deck");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetDeactivatedDecks() {
        var decks = Lists.newArrayList(deckService.getDisactivatedDecks());
        Assertions.assertEquals(1, decks.size(),
                "Insufficient amount of decks initialized for test data source");

        Assertions.assertEquals(UUID.fromString("4af32892-5e68-49be-9c7c-84fa54140880"), decks.get(0).getDeckID());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDeleteEmptyDeck() {
        var user = userService.loadUser("user1").get();
        Assertions.assertEquals(3, Lists.newArrayList(deckService.getOwnedDecks(user)).size(),
                "Insufficient amount of decks initialized for test data source");

        var deck1 = deckService.loadDeck(UUID.fromString("9ec2dda8-3850-4ffd-bc25-fabc732adaec")).get();

        deckService.deleteDeck(deck1);

        Assertions.assertEquals(2, Lists.newArrayList(deckService.getOwnedDecks(user)).size(),
                "Not the right amount of decks left...");
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDeleteNonEmptyDeck() {
        var user = userService.loadUser("user1").get();
        Assertions.assertEquals(3, Lists.newArrayList(deckService.getOwnedDecks(user)).size(),
                "Insufficient amount of decks initialized for test data source");

        var deck1 = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();

        deckService.deleteDeck(deck1);

        Assertions.assertEquals(2, Lists.newArrayList(deckService.getOwnedDecks(user)).size(),
                "Not the right amount of decks left...");
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user2", authorities = {"USER"})
    public void testDeleteDeckUnAuthorized() {
        var deck1 = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();

        Assertions.assertThrows(
                org.springframework.security.access.AccessDeniedException.class,
                () -> {
                    deckService.deleteDeck(deck1);
                });
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateDeck() {
        var user = userService.loadUser("user1").get();
        Assertions.assertEquals(3, Lists.newArrayList(deckService.getOwnedDecks(user)).size(),
                "Insufficient amount of decks initialized for test data source");

        var deck = new Deck();
        deck.setName("New Test Deck");
        deck.setDescription("This is a deck for testing");
        deck.setPublic(false);
        deck.setActive(true);

        try {
            Assertions.assertEquals(deck, deckService.createDeck(deck, user));
        } catch (DuplicateDeckException | MissingNameException | MissingDescritionException e) {
            Assertions.fail("Exception was thrown which is not supposed to happen!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(4, Lists.newArrayList(deckService.getOwnedDecks(user)).size(),
                "Not the right amount of decks after operation");
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDuplicateDeck() {
        var user = userService.loadUser("user1").get();
        var deck = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();

        Assertions.assertThrows(DuplicateDeckException.class, () -> deckService.createDeck(deck, user));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateDeck_NameEmpty() {
        var user = userService.loadUser("user1").get();
        var deck = new Deck();
        deck.setOwner(user);
        deck.setDescription("Description");

        Assertions.assertThrows(MissingNameException.class, () -> deckService.createDeck(deck, user));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateDeck_NameEmpty2() {
        var user = userService.loadUser("user1").get();
        var deck = new Deck();
        deck.setOwner(user);
        deck.setName("");
        deck.setDescription("Description");

        Assertions.assertThrows(MissingNameException.class, () -> deckService.createDeck(deck, user));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateDeck_DescriptionEmpty() {
        var user = userService.loadUser("user1").get();
        var deck = new Deck();
        deck.setOwner(user);
        deck.setName("Name");

        Assertions.assertThrows(MissingDescritionException.class, () -> deckService.createDeck(deck, user));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateDeck_DescriptionEmpty2() {
        var user = userService.loadUser("user1").get();
        var deck = new Deck();
        deck.setOwner(user);
        deck.setName("Name");
        deck.setDescription("");

        Assertions.assertThrows(MissingDescritionException.class, () -> deckService.createDeck(deck, user));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testPublishDeck() {

        var deck = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();

        try {
            var newDeck = deckService.publishDeck(deck);
            Assertions.assertEquals(deck, newDeck);
            Assertions.assertTrue(newDeck.isPublic());
        } catch (NoSuchDeckException e) {
            Assertions.fail("Exception was thrown which is not supposed to happen!\n" + e.getMessage());
        }

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUnPublishDeck() {

        var deck = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();

        try {
            var newDeck = deckService.unpublishDeck(deck);
            Assertions.assertEquals(deck, newDeck);
            Assertions.assertFalse(newDeck.isPublic());
        } catch (NoSuchDeckException e) {
            Assertions.fail("Exception was thrown which is not supposed to happen!\n" + e.getMessage());
        }

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testActivateDeck() {

        var deck = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();

        try {
            var newDeck = deckService.activateDeck(deck);
            Assertions.assertEquals(deck, newDeck);
            Assertions.assertTrue(newDeck.isActive());
        } catch (NoSuchDeckException e) {
            Assertions.fail("Exception was thrown which is not supposed to happen!\n" + e.getMessage());
        }

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDeactivateDeck() {
        var deck = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();

        try {
            var newDeck = deckService.deactivateDeck(deck);
            Assertions.assertEquals(deck, newDeck);
            Assertions.assertFalse(newDeck.isActive());
        } catch (NoSuchDeckException e) {
            Assertions.fail("Exception was thrown which is not supposed to happen!\n" + e.getMessage());
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeactivateDeckAdmin() {
        var deck = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();

        try {
            var newDeck = deckService.deactivateDeck(deck);
            Assertions.assertEquals(deck, newDeck);
            Assertions.assertFalse(newDeck.isActive());
        } catch (NoSuchDeckException e) {
            Assertions.fail("Exception was thrown which is not supposed to happen!\n" + e.getMessage());
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUpdateDeck() {

        var deck = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();

        deck.setName("Not " + deck.getName() + " Anymore");

        try {
            Assertions.assertEquals(deck, deckService.updateDeck(deck));
        } catch (NoSuchDeckException e) {
            Assertions.fail("Exception was thrown which is not supposed to happen!\n" + e.getMessage());
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUpdateDeck_throwsNoSuchDeck() {
        var user = userService.loadUser("user1").orElse(null);
        var deck = new Deck();
        deck.setOwner(user);

        Assertions.assertThrows(NoSuchDeckException.class, () -> deckService.updateDeck(deck));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testGetPublicDecks() {
        var publicDecks = deckService.getPublicDecks();
        Assertions.assertEquals(3, Lists.newArrayList(publicDecks).size(),
                "Insufficient amount of decks initialized for test data source");

        for (Deck deck : publicDecks) {
            if (deck.getId() == null) {
                Assertions.fail("A deck with Id null apeared, that should never happen");
            }
            switch (deck.getId().toString()) {
                case "22a0c39d-a2b0-460d-9e39-4dc2db3e7b36":
                case "9ec2dda8-3850-4ffd-bc25-fabc732adaec":
                case "19534ec0-bf13-4317-bb3d-91c29603b06c":
                    break;
                default:
                    Assertions.fail("There is an deck that should not be here: "
                            + deck.getId().toString());
            }
        }
    }

}
