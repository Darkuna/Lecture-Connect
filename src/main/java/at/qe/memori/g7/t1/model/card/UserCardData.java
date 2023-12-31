package at.qe.memori.g7.t1.model.card;

import at.qe.memori.g7.t1.model.deck.UserDeckData;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
public class UserCardData implements Persistable<UUID>, Serializable, Comparable<UserCardData> {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID userCardDataID;

    @Column(nullable = false)
    private int repetitions;

    @Column(nullable = false)
    private double eFactor;

    @Column
    @Temporal(TemporalType.DATE)
    private Date nextDate;

    @Column(nullable = false)
    private int learnInterval;

    @Column(nullable = false)
    private Difficulty difficultyRating;

    @Column(nullable = false)
    private boolean reversed;

    @ManyToOne
    @JoinColumn(name = "deck_data_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserDeckData userDeckData;

    @ManyToOne
    @JoinColumn(name = "card_card_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Card card;

    public UserCardData(UserDeckData userDeckData, Card card, boolean reversed) {
        this.userCardDataID = UUID.randomUUID();
        this.reversed = reversed;

        this.card = card;
        this.userDeckData = userDeckData;
        this.difficultyRating = Difficulty.NEW;

        this.repetitions = 0;
        this.learnInterval = 0;
        this.nextDate = new Date();
        this.eFactor = 2.5D;
    }

    /**
     * updates the userCardDate to contain all the information that is callculated
     * CALCULATIONS:
     * - repetitions
     * - interval
     * - nextLearn date
     * <p>
     * returns a boolean value that says, if the card should be enqueued again
     *
     * @param newDifficulty
     */
    public boolean vote(Difficulty newDifficulty) {
        this.repetitions++;
        this.difficultyRating = newDifficulty;

        int q = difficultyRating.ordinal();

        // the answer is not right
        if (q < 3) {
            learnInterval = 1;
        } else {

            if (repetitions == 1) {
                learnInterval = 1;
            } else if (repetitions == 2) {
                learnInterval = 6;
            } else {
                learnInterval *= eFactor;
                this.eFactor = Math.max(1.3, eFactor - 0.8 + 0.28 * q - 0.02 * Math.pow(q, 2));
            }

            addDays(learnInterval);
        }

        // returns if this card was studies sufficiently
        return q < 4;
    }

    private void addDays(int days) {
        nextDate = new Date(System.currentTimeMillis() + (long) days * 24 * 3600 * 1000);
    }

    public UserCardData() {

    }

    @Override
    public int compareTo(UserCardData o) {
        return this.userCardDataID.compareTo(o.getUserCardDataID());
    }

    @Override
    public UUID getId() {
        return getUserCardDataID();
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

        UserCardData that = (UserCardData) o;
        return Objects.equals(userCardDataID, that.userCardDataID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userCardDataID, repetitions, eFactor, nextDate, learnInterval, difficultyRating, card);
    }

    public UUID getUserCardDataID() {
        return userCardDataID;
    }

    public void setUserCardDataID(UUID userCardDataID) {
        this.userCardDataID = userCardDataID;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public double getEFactor() {
        return eFactor;
    }

    public void setEFactor(double eFactor) {
        this.eFactor = eFactor;
    }

    public Date getNextDate() {
        return nextDate;
    }

    public void setNextDate(Date nextDate) {
        this.nextDate = nextDate;
    }

    public int getLearnInterval() {
        return learnInterval;
    }

    public void setLearnInterval(int interval) {
        this.learnInterval = interval;
    }

    public Difficulty getDifficultyRating() {
        return difficultyRating;
    }

    public void setDifficultyRating(Difficulty difficultyRating) {
        this.difficultyRating = difficultyRating;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }
}
