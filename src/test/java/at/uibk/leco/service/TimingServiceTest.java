package at.uibk.leco.service;

import at.uibk.leco.models.RoomTable;
import at.uibk.leco.models.Timing;
import at.uibk.leco.models.enums.Day;
import at.uibk.leco.models.enums.TimingType;
import at.uibk.leco.services.RoomTableService;
import at.uibk.leco.services.TimingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
@WebAppConfiguration
public class TimingServiceTest {
    @Autowired
    private TimingService timingService;
    @Autowired
    private RoomTableService roomTableService;

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateTimingWithCorrectParameters(){
        Day day = Day.MONDAY;
        LocalTime start = LocalTime.of(12,0);
        LocalTime end = LocalTime.of(14,0);
        Timing timing = timingService.createTiming(start, end, day, TimingType.ASSIGNED);

        assertEquals(day, timing.getDay());
        assertEquals(start, timing.getStartTime());
        assertEquals(end, timing.getEndTime());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateTimingWithWrongParameters(){
        Day day = Day.MONDAY;
        LocalTime start = LocalTime.of(14,0);
        LocalTime end = LocalTime.of(12,0);

        assertThrows(IllegalArgumentException.class, () -> timingService.createTiming(start, end, day,
                TimingType.ASSIGNED));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateTimingWithEndTimeAfterGlobalEndTime(){
        Day day = Day.MONDAY;
        LocalTime start = LocalTime.of(12,0);
        LocalTime end = LocalTime.of(22,0);

        assertThrows(IllegalArgumentException.class, () -> timingService.createTiming(start, end, day,
                TimingType.ASSIGNED));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateTimingFromTimingObjectWithCorrectParameters(){
        Timing timing = new Timing();
        timing.setDay(Day.MONDAY);
        timing.setStartTime(LocalTime.of(12,0));
        timing.setEndTime(LocalTime.of(14,0));

        timing = timingService.createTiming(timing);

        assertEquals(Day.MONDAY, timing.getDay());
        assertEquals(LocalTime.of(12,0), timing.getStartTime());
        assertEquals(LocalTime.of(14,0), timing.getEndTime());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateTimingFromTimingObjectWithWrongParameters(){
        Timing timing = new Timing();
        timing.setDay(Day.MONDAY);
        timing.setStartTime(LocalTime.of(14,0));
        timing.setEndTime(LocalTime.of(12,0));

        assertThrows(IllegalArgumentException.class, () -> timingService.createTiming(timing));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateTimingFromTimingObjectWithEndTimeAfterGlobalEndTime(){
        Timing timing = new Timing();
        timing.setDay(Day.MONDAY);
        timing.setStartTime(LocalTime.of(14,0));
        timing.setEndTime(LocalTime.of(22,0));

        assertThrows(IllegalArgumentException.class, () -> timingService.createTiming(timing));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimingConstraintsOfRoomTable(){
        int numberOfConstraints = 7;
        RoomTable roomTable = roomTableService.loadRoomTableByID(-90);
        List<Timing> timingConstraints = timingService.loadTimingConstraintsOfRoomTable(roomTable);

        assertEquals(numberOfConstraints, timingConstraints.size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimingConstraintsOfCourse(){
        String courseId = "703010";
        int numberOfConstraints = 2;
        List<Timing> timingConstraints = timingService.loadTimingConstraintsOfCourse(courseId);

        assertEquals(numberOfConstraints, timingConstraints.size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimingByID(){
        Timing timing = timingService.loadTimingByID(-1);
        assertNotNull(timing);
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimingWithWrongID(){
        assertThrows(EntityNotFoundException.class, () -> timingService.loadTimingByID(-1000));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDeleteTimingConstraints(){
        RoomTable roomTable = roomTableService.loadRoomTableByID(-91);
        List<Timing> timingConstraints = roomTable.getTimingConstraints();
        int numberOfTimingConstraints = timingConstraints.size();

        assertTrue(numberOfTimingConstraints > 0);

        timingService.deleteTimingConstraints(timingConstraints);

        roomTable = roomTableService.loadRoomTableByID(-91);
        timingConstraints = roomTable.getTimingConstraints();
        numberOfTimingConstraints = timingConstraints.size();

        assertEquals(0, numberOfTimingConstraints);
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    @DirtiesContext
    public void testDeleteTiming(){
        Timing timing = timingService.loadTimingByID(-1);
        timingService.deleteTiming(timing);

        assertThrows(EntityNotFoundException.class, () -> timingService.loadTimingByID(-1));

    }


}
