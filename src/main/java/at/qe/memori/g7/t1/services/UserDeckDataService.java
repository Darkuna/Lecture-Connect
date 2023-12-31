package at.qe.memori.g7.t1.services;

import at.qe.memori.g7.t1.model.Userx;
import at.qe.memori.g7.t1.model.card.Card;
import at.qe.memori.g7.t1.model.card.UserCardData;
import at.qe.memori.g7.t1.model.deck.Deck;
import at.qe.memori.g7.t1.model.deck.UserDeckData;
import at.qe.memori.g7.t1.repositories.CardRepository;
import at.qe.memori.g7.t1.repositories.UserCardDataRepository;
import at.qe.memori.g7.t1.repositories.UserDeckDataRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@Scope("application")
public class UserDeckDataService {
    private final UserDeckDataRepository userDeckDataRepository;
    private final CardRepository cardRepository;
    private final UserCardDataRepository userCardDataRepository;

    public UserDeckDataService(UserDeckDataRepository userDeckDataRepository, CardRepository cardRepository, UserCardDataRepository userCardDataRepository) {
        this.userDeckDataRepository = userDeckDataRepository;
        this.cardRepository = cardRepository;
        this.userCardDataRepository = userCardDataRepository;
    }

    public Iterable<UserDeckData> get(Userx user) {
        return userDeckDataRepository.findByUser(user);
    }

    public Iterable<UserDeckData> get(Deck deck) {
        return userDeckDataRepository.findByDeck(deck);
    }

    public UserDeckData createUserDeckData(UserDeckData userDeckData) {

        var createdUserDeckData = userDeckDataRepository.save(userDeckData);

        var cards = cardRepository.findByDeckIncluded(userDeckData.getDeck());
        cards.spliterator()
                .forEachRemaining(
                        (Card card) -> {
                            userCardDataRepository.save(new UserCardData(userDeckData, card, false));

                            if (card.isReversible()) {
                                userCardDataRepository.save(new UserCardData(userDeckData, card, true));
                            }
                        });

        return createdUserDeckData;
    }

    public void deleteUserDeckData(UserDeckData userDeckData) {
        userDeckDataRepository.delete(userDeckData);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public long size() {
        return userDeckDataRepository.count();
    }

    public boolean isBookmarked(Userx currentUser, Deck deck) {
        return userDeckDataRepository.findByUserAndDeck(currentUser, deck).isPresent();
    }

}
