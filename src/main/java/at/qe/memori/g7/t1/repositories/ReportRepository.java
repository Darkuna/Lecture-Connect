package at.qe.memori.g7.t1.repositories;

import at.qe.memori.g7.t1.model.Report;
import at.qe.memori.g7.t1.model.Userx;
import at.qe.memori.g7.t1.model.deck.Deck;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ReportRepository extends AbstractRepository<Report, UUID> {
    Iterable<Report> findByDeck(Deck deck);

    @Query("select r from Report r where r.reporter = ?1 and r.deck = ?2")
    Iterable<Report> findByReporterAndDeck(Userx reporter, Deck deck);
}
