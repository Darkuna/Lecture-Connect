package at.uibk.leco.service;

import at.uibk.leco.models.*;
import at.uibk.leco.models.enums.ChangeType;
import at.uibk.leco.models.GlobalTableChange;
import at.uibk.leco.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("dev")
public class GlobalTableChangeServiceTest {
    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private GlobalTableChangeService globalTableChangeService;

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateGlobalTableChangeAndLoad() {
        TimeTable timeTable = timeTableService.loadTimeTable(-1);

        List<GlobalTableChange> changes = globalTableChangeService.loadAllByTimeTable(timeTable);

        assertEquals(0, changes.size());

        globalTableChangeService.create(ChangeType.ASSIGN_COURSE, timeTable, "Test description");
        changes = globalTableChangeService.loadAllByTimeTable(timeTable);

        assertEquals(1, changes.size());
    }
}
