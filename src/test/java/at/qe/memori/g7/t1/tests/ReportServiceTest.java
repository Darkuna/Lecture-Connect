package at.qe.memori.g7.t1.tests;

import at.qe.memori.g7.t1.exceptions.report.DuplicateReportException;
import at.qe.memori.g7.t1.model.Report;
import at.qe.memori.g7.t1.model.ReportState;
import at.qe.memori.g7.t1.repositories.ReportRepository;
import at.qe.memori.g7.t1.services.DeckService;
import at.qe.memori.g7.t1.services.ReportService;
import at.qe.memori.g7.t1.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Some very basic tests for {@link UserService}.
 */
@SpringBootTest
@WebAppConfiguration
public class ReportServiceTest {

    @Autowired
    ReportService reportService;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    DeckService deckService;

    @Autowired
    UserService userService;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDataInitialization() {
        Assertions.assertEquals(3, reportService.size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetAllReports() {
        var user = userService.loadUser("admin").get();
        var deck = deckService.loadDeck(UUID.fromString("9ec2dda8-3850-4ffd-bc25-fabc732adaec")).get();

        Report report = new Report("Test Report", ReportState.REPORTED, user, deck);

        // Save the test report
        reportService.saveReport(report);

        // Retrieve all reports
        Iterable<Report> allReports = reportService.getAllReports();

        // Assert that allReports is not null and contains at least one report
        assertNotNull(allReports);
        assertTrue(allReports.iterator().hasNext());

        // Assert that the test report is present in allReports
        boolean found = false;
        for (Report r : allReports) {
            if (r.getReportID().equals(report.getReportID())) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeleteUserReport() {
        var user = userService.loadUser("admin").get();
        var deck = deckService.loadDeck(UUID.fromString("9ec2dda8-3850-4ffd-bc25-fabc732adaec")).get();

        Report report = new Report("Test Report", ReportState.REPORTED, user, deck);

        // Save the test report
        reportService.saveReport(report);

        // Assert that the test report is present in the repository
        assertTrue(reportRepository.findById(report.getReportID()).isPresent());

        // Delete the test report
        reportService.deleteUserReport(report);

        // Assert that the test report is no longer present in the repository
        assertFalse(reportRepository.findById(report.getReportID()).isPresent());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateReport() throws DuplicateReportException {
        var user = userService.loadUser("user1").get();
        var deck = deckService.loadDeck(UUID.fromString("9ec2dda8-3850-4ffd-bc25-fabc732adaec")).get();

        Report report = new Report("Test Report", ReportState.REPORTED, user, deck);

        // Create a new report
        Report createdReport = reportService.createReport(report, user);

        // Assert that the created report is not null
        assertNotNull(createdReport);

        // Retrieve the report from the repository by its ID
        Optional<Report> foundReport = reportRepository.findById(createdReport.getReportID());

        // Assert that the found report is present and has the same attributes as the test report
        assertTrue(foundReport.isPresent());
        assertEquals(report.getReason(), foundReport.get().getReason());
        assertEquals(report.getState(), foundReport.get().getState());
        assertEquals(report.getDeck(), foundReport.get().getDeck());
        assertEquals(report.getReporter(), foundReport.get().getReporter());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateReport_DuplicateReport() {
        var user = userService.loadUser("user1").get();
        var deck = deckService.loadDeck(UUID.fromString("9ec2dda8-3850-4ffd-bc25-fabc732adaec")).get();

        Report report = new Report("Test Report", ReportState.REPORTED, user, deck);

        // set id to something that exists
        report.setReportID(UUID.fromString("a20cea9f-b35e-411a-b15c-d31700d66a83"));

        // Create a new report
        Assertions.assertThrows(DuplicateReportException.class, () -> reportService.createReport(report, user));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testSaveReport() {
        var user = userService.loadUser("user1").get();
        var deck = deckService.loadDeck(UUID.fromString("9ec2dda8-3850-4ffd-bc25-fabc732adaec")).get();

        Report report = new Report("Test Report", ReportState.REPORTED, user, deck);

        // Save the test report
        Report savedReport = reportService.saveReport(report);

        // Assert that the saved report is not null
        assertNotNull(savedReport);

        // Retrieve the report from the repository by its ID
        Optional<Report> foundReport = reportRepository.findById(savedReport.getReportID());

        // Assert that the found report is present and has the same attributes as the test report
        assertTrue(foundReport.isPresent());
        assertEquals(report.getReason(), foundReport.get().getReason());
        assertEquals(report.getState(), foundReport.get().getState());
        assertEquals(report.getDeck(), foundReport.get().getDeck());
        assertEquals(report.getReporter(), foundReport.get().getReporter());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testFindByReporter() throws DuplicateReportException {
        var user = userService.loadUser("user1").get();
        var deck = deckService.loadDeck(UUID.fromString("9ec2dda8-3850-4ffd-bc25-fabc732adaec")).get();

        Report report = new Report("Test Report", ReportState.REPORTED, user, deck);

        // Create and save a test report
        reportService.createReport(report, user);

        // Find the report by its reporter and deck
        Iterable<Report> foundReports = reportService.findByReporter(user, deck);

        // Assert that the found reports are not null and there is at least one report returned
        assertNotNull(foundReports);
        assertTrue(foundReports.iterator().hasNext());

        // Get the last report returned
        Iterator<Report> it = foundReports.iterator();
        Report foundReport = it.next();
        while (it.hasNext())
            foundReport = it.next();

        // Assert that the found report has the same attributes as the test report
        assertEquals(report.getReason(), foundReport.getReason());
        assertEquals(report.getState(), foundReport.getState());
        assertEquals(report.getDeck(), foundReport.getDeck());
        assertEquals(report.getReporter(), foundReport.getReporter());
    }


}
