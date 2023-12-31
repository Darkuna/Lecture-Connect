package at.qe.memori.g7.t1.services;

import at.qe.memori.g7.t1.model.card.Card;
import at.qe.memori.g7.t1.model.card.UserCardData;
import at.qe.memori.g7.t1.model.deck.UserDeckData;
import at.qe.memori.g7.t1.repositories.UserCardDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Scope("application")
public class UserCardDataService {

    @Autowired
    UserCardDataRepository userCardDataRepository;

    public Queue<UserCardData> getLearnableCards(UserDeckData userDeckData) {
        var queue = new LinkedList<UserCardData>();
        queue.addAll((Collection<UserCardData>) userCardDataRepository.getLearnableCards(userDeckData));
        return queue;
    }

    /**
     * Returns an iterable of all userCardData entities.
     *
     * @return long
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public long size() {
        return userCardDataRepository.count();
    }

    public Iterable<UserCardData> get(UserDeckData userDeckData) {
        return userCardDataRepository.findByUserDeckData(userDeckData);
    }

    public Iterable<UserCardData> getByCard(Card card) {
        return userCardDataRepository.findByCard(card);
    }

    public UserCardData saveUserCardData(UserCardData userCardData) {
        return userCardDataRepository.save(userCardData);
    }

    public Optional<UserCardData> get(UUID id) {
        return userCardDataRepository.findById(id);
    }
}
