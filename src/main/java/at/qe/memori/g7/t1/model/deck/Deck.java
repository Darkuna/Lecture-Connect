package at.qe.memori.g7.t1.model.deck;

import at.qe.memori.g7.t1.model.Userx;
import at.qe.memori.g7.t1.model.card.Card;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Deck implements Persistable<UUID>, Serializable, Comparable<Deck> {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID deckID = UUID.randomUUID();

    @Column(length = 512)
    private String name;

    @Column(length = 512)
    private String description;

    @Column(length = 1)
    private boolean isPublic;

    @Column(length = 1)
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "owner_username", columnDefinition = "TEXT")
    private Userx owner;

    @OneToMany(mappedBy = "deckIncluded", cascade = CascadeType.ALL)
    private List<Card> cards;

    public Deck(String name, String description, boolean isPublic, boolean isActive, Userx owner) {
        this.deckID = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.isActive = isActive;
        this.owner = owner;
    }

    public Deck() {
        this.deckID = UUID.randomUUID();
        this.isActive = true;
        this.isPublic = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Deck other = (Deck) o;
        return Objects.equals(this.deckID, other.deckID);
    }

    @Override
    public int hashCode() {
        return deckID.hashCode();
    }

    @Override
    public int compareTo(Deck o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public UUID getId() {
        return getDeckID();
    }

    @Override
    public boolean isNew() {
        return false;
    }

    public UUID getDeckID() {
        return deckID;
    }

    public void setDeckID(UUID deckID) {
        this.deckID = deckID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Userx getOwner() {
        return owner;
    }

    public void setOwner(Userx owner) {
        this.owner = owner;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
