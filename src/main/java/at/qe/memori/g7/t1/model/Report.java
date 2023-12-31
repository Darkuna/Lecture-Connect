package at.qe.memori.g7.t1.model;

import at.qe.memori.g7.t1.model.deck.Deck;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class Report implements Persistable<UUID>, Serializable, Comparable<Report> {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID reportID = UUID.randomUUID();

    @Column(length = 512)
    private String reason;

    @Column(length = 1)
    private ReportState state;

    @ManyToOne
    @JoinColumn(name = "reporter_username", columnDefinition = "TEXT")
    private Userx reporter;

    @ManyToOne
    @JoinColumn(name = "deck_deck_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Deck deck;

    public Report(String reason, ReportState state, Userx reporter, Deck deck) {
        this.reportID = UUID.randomUUID();
        this.reason = reason;
        this.state = state;
        this.reporter = reporter;
        this.deck = deck;
    }

    public Report() {
        this.reportID = UUID.randomUUID();
        this.state = ReportState.REPORTED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Report report = (Report) o;

        if (!getReportID().equals(report.getReportID())) return false;
        if (getReason() != null ? !getReason().equals(report.getReason()) : report.getReason() != null) return false;
        if (getState() != report.getState()) return false;
        if (getReporter() != null ? !getReporter().equals(report.getReporter()) : report.getReporter() != null)
            return false;
        return getDeck() != null ? getDeck().equals(report.getDeck()) : report.getDeck() == null;
    }

    @Override
    public int hashCode() {
        int result = getReportID().hashCode();
        result = 31 * result + (getReason() != null ? getReason().hashCode() : 0);
        result = 31 * result + (getState() != null ? getState().hashCode() : 0);
        result = 31 * result + (getReporter() != null ? getReporter().hashCode() : 0);
        result = 31 * result + (getDeck() != null ? getDeck().hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Report o) {
        return this.reportID.compareTo(o.reportID);
    }


    public UUID getReportID() {
        return reportID;
    }

    public void setReportID(UUID reportID) {
        this.reportID = reportID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ReportState getState() {
        return state;
    }

    public void setState(ReportState state) {
        this.state = state;
    }

    public Userx getReporter() {
        return reporter;
    }

    public void setReporter(Userx reporter) {
        this.reporter = reporter;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    @Override
    public UUID getId() {
        return getReportID();
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
