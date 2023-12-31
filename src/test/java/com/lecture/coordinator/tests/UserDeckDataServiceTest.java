package com.lecture.coordinator.tests;

import at.qe.memori.g7.t1.model.deck.Deck;
import at.qe.memori.g7.t1.model.deck.UserDeckData;
import at.qe.memori.g7.t1.services.*;
import com.lecture.coordinator.services.UserService;
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
import java.util.UUID;

@SpringBootTest
@WebAppConfiguration
public class UserDeckDataServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    UserDeckDataService userDeckDataService;

    @Autowired
    UserCardDataService userCardDataService;

    @Autowired
    CardService cardService;

    @Autowired
    DeckService deckService;

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testDataInitialization() {
        Assertions.assertEquals(2, userDeckDataService.size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testGetUserDeckDataFromDeck() {
        Deck deck = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36")).get();
        Iterable<UserDeckData> userDeckDatas = userDeckDataService.get(deck);

        List<UUID> userDeckDataIds = new ArrayList<>();
        userDeckDataIds.add(UUID.fromString("9fdaf57d-d2c3-44d8-979b-8df0acf96cf7"));
        userDeckDataIds.add(UUID.fromString("3e4ab8a8-5e96-4db4-9708-e3f549d63006"));

        Assertions.assertEquals(userDeckDataIds.size(), Lists.newArrayList(userDeckDatas).size());

        for (UserDeckData deckData : userDeckDatas) {
            Assertions.assertTrue(userDeckDataIds.contains(deckData.getId()));
        }
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testGetUserDeckDataFromUser() {
        var userOpt = userService.loadUser("user1");
        if (userOpt.isEmpty()) {
            Assertions.fail("Initialization failed or test data corrupted");
        }
        var user1 = userOpt.get();
        Iterable<UserDeckData> userDeckDatas = userDeckDataService.get(user1);

        List<UUID> userDeckDataIds = new ArrayList<>();
        userDeckDataIds.add(UUID.fromString("9fdaf57d-d2c3-44d8-979b-8df0acf96cf7"));

        Assertions.assertEquals(userDeckDataIds.size(), Lists.newArrayList(userDeckDatas).size());

        for (UserDeckData deckData : userDeckDatas) {
            Assertions.assertTrue(userDeckDataIds.contains(deckData.getId()));
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testCreateUserDeckData() {
        var userOpt = userService.loadUser("user1");
        if (userOpt.isEmpty()) {
            Assertions.fail("Initialization failed or test data corrupted");
        }
        var user = userOpt.get();
        var userDeckDatas = userDeckDataService.get(user);

        int countBefore = Lists.newArrayList(userDeckDatas).size();

        var deckOpt = deckService.loadDeck(UUID.fromString("19534ec0-bf13-4317-bb3d-91c29603b06c"));
        if (deckOpt.isEmpty()) {
            Assertions.fail("Initialization failed or test data corrupted");
        }
        var deck = deckOpt.get();


        var newUDD = new UserDeckData(user, deck);

        var storedUDD = userDeckDataService.createUserDeckData(newUDD);

        userDeckDatas = userDeckDataService.get(user);

        int countAfter = Lists.newArrayList(userDeckDatas).size();

        // check if there is 1 UserDeckData more after the operation
        Assertions.assertEquals(countBefore + 1, countAfter);

        // check if the deck is bookmarked now
        Assertions.assertTrue(userDeckDataService.isBookmarked(user, deck));

        // check if the userCard data is getting bigger as expected
        var newUserCardDatas = userCardDataService.get(storedUDD);

        int countCardData = Lists.newArrayList(newUserCardDatas).size();

        int expectedCardDataCount = 0;

        for (var card : cardService.get(deck)) {
            expectedCardDataCount += card.isReversible() ? 2 : 1;
        }

        Assertions.assertEquals(expectedCardDataCount, countCardData);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testDeleteUserDeckData() {
        var userOpt = userService.loadUser("user1");
        if (userOpt.isEmpty()) {
            Assertions.fail("Initialization failed or test data corrupted");
        }
        var user = userOpt.get();
        var userDeckDatas = userDeckDataService.get(user);

        int countBefore = Lists.newArrayList(userDeckDatas).size();

        var toDelete = userDeckDatas.iterator().next();

        userDeckDataService.deleteUserDeckData(toDelete);

        userDeckDatas = userDeckDataService.get(user);

        int countAfter = Lists.newArrayList(userDeckDatas).size();

        // check if there is 1 UserDeckData less after the operation
        Assertions.assertEquals(countBefore - 1, countAfter);

        // check that it is not bookmarked anymore
        Assertions.assertFalse(userDeckDataService.isBookmarked(user, toDelete.getDeck()));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testIsBookmarked_true() {
        var userOpt = userService.loadUser("user1");
        if (userOpt.isEmpty()) {
            Assertions.fail("Initialization failed or test data corrupted");
        }
        var user = userOpt.get();

        var deckOpt = deckService.loadDeck(UUID.fromString("22a0c39d-a2b0-460d-9e39-4dc2db3e7b36"));
        if (deckOpt.isEmpty()) {
            Assertions.fail("Initialization failed or test data corrupted");
        }
        var deck = deckOpt.get();

        Assertions.assertTrue(userDeckDataService.isBookmarked(user, deck));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testIsBookmarked_false() {
        var userOpt = userService.loadUser("user1");
        if (userOpt.isEmpty()) {
            Assertions.fail("Initialization failed or test data corrupted");
        }
        var user = userOpt.get();

        var deckOpt = deckService.loadDeck(UUID.fromString("cb5e5b13-fd0e-4be0-a29e-7d26800e94bb"));
        if (deckOpt.isEmpty()) {
            Assertions.fail("Initialization failed or test data corrupted");
        }
        var deck = deckOpt.get();

        Assertions.assertFalse(userDeckDataService.isBookmarked(user, deck));
    }
}
