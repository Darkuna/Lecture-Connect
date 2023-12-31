package at.qe.memori.g7.t1.model.card;

import at.qe.memori.g7.t1.model.deck.Deck;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Card implements Persistable<UUID>, Serializable, Comparable<Card> {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID cardID;

    @Column(length = 512)
    // question of the card (front side text)
    private String front;

    @Column(length = 512)
    // answer to the question (back side of the card)
    private String back;

    @Column(length = 1)
    private boolean isReversible;

    @ManyToOne
    @JoinColumn(name = "deck_included_uuid", nullable = false)
    private Deck deckIncluded;

    public Card(String front, String back, boolean isReversible, Deck deckIncluded) {
        this.cardID = UUID.randomUUID();
        this.front = front;
        this.back = back;
        this.isReversible = isReversible;
        this.deckIncluded = deckIncluded;
    }

    public Card() {
        this.cardID = UUID.randomUUID();
        this.isReversible = false;
    }

    @Override
    public int compareTo(Card o) {
        return this.cardID.compareTo(o.getCardID());
    }

    @Override
    public UUID getId() {
        return getCardID();
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

        final Card other = (Card) o;

        return Objects.equals(this.cardID, other.cardID);
    }

    @Override
    public int hashCode() {
        return cardID.hashCode();
    }

    public UUID getCardID() {
        return cardID;
    }

    public void setCardID(UUID cardID) {
        this.cardID = cardID;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public boolean isReversible() {
        return isReversible;
    }

    public void setReversible(boolean isReversible) {
        this.isReversible = isReversible;
    }

    public Deck getDeckIncluded() {
        return deckIncluded;
    }

    public void setDeckIncluded(Deck deckIncluded) {
        this.deckIncluded = deckIncluded;
    }
}
