package at.qe.memori.g7.t1.model.deck;

import at.qe.memori.g7.t1.model.Userx;
import at.qe.memori.g7.t1.model.card.Difficulty;
import at.qe.memori.g7.t1.model.card.UserCardData;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class UserDeckData implements Persistable<UUID>, Serializable, Comparable<UserDeckData> {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID userDeckDataID = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "deck_deck_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Deck deck;

    @OneToMany(mappedBy = "userDeckData", cascade = CascadeType.ALL)
    private List<UserCardData> userCardDataList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "username")
    private Userx user;

    public UserDeckData(Userx user, Deck deck) {
        this.user = user;
        this.deck = deck;
    }

    public UserDeckData() {
    }

    public void addUserCardData(UserCardData addData) {
        userCardDataList.add(addData);
    }

    public int getNewCardsCount() {
        int newCardsCount = 0;
        for (UserCardData ucd : userCardDataList) {
            if (ucd.getDifficultyRating() == Difficulty.NEW) {
                newCardsCount++;
            }
        }
        return newCardsCount;
    }

    public int getCardsCount() {
        return userCardDataList.size();
    }

    @Override
    public int compareTo(UserDeckData o) {
        return this.userDeckDataID.compareTo(o.getUserDeckDataID());
    }

    @Override
    public UUID getId() {
        return getUserDeckDataID();
    }

    @Override
    public boolean isNew() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDeckData that = (UserDeckData) o;
        return Objects.equals(userDeckDataID, that.userDeckDataID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userDeckDataID);
    }

    public UUID getUserDeckDataID() {
        return userDeckDataID;
    }

    public void setUserDeckDataID(UUID userDeckDataID) {
        this.userDeckDataID = userDeckDataID;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Userx getUser() {
        return user;
    }

    public void setUser(Userx user) {
        this.user = user;
    }

    public List<UserCardData> getUserCardDataList() {
        return userCardDataList;
    }

    public void setUserCardDataList(List<UserCardData> userCardDataList) {
        this.userCardDataList = userCardDataList;
    }

}