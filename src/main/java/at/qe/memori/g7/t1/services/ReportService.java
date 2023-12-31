package at.qe.memori.g7.t1.services;

import at.qe.memori.g7.t1.exceptions.deck.DuplicateDeckException;
import at.qe.memori.g7.t1.exceptions.report.DuplicateReportException;
import at.qe.memori.g7.t1.model.Report;
import at.qe.memori.g7.t1.model.Userx;
import at.qe.memori.g7.t1.model.deck.Deck;
import at.qe.memori.g7.t1.repositories.ReportRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@Scope("application")
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Returns an iterable of all report entities.
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Iterable<Report> getAllReports() {
        return reportRepository.findAll();
    }

    /**
     * delete a report
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUserReport(Report report) {
        reportRepository.delete(report);
    }

    /**
     * method to save a report
     * can be used to update or create a report
     *
     * @param report
     * @return
     * @throws DuplicateDeckException
     */
    public Report createReport(Report report, Userx currentUser)
            throws DuplicateReportException {
        if (reportRepository.findById(report.getReportID()).isPresent()) {
            throw new DuplicateReportException("Cannot create report, such ID already exists");
        }

        report.setReporter(currentUser);
        return saveReport(report);
    }

    /**
     * method to save a report
     * can be used to update or create a report
     *
     * @param report
     * @return
     */
    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }

    public Iterable<Report> findByReporter(Userx user, Deck deck) {
        return reportRepository.findByReporterAndDeck(user, deck);
    }

    /**
     * Returns the length of all report entities.
     *
     * @return long
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public long size() {
        return reportRepository.count();
    }
}
