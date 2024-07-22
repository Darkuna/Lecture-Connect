package com.example.demo.service;

import com.example.demo.constants.TimingConstants;
import com.example.demo.models.*;
import com.example.demo.models.enums.Day;
import com.example.demo.models.enums.Semester;
import com.example.demo.models.enums.TimingType;
import com.example.demo.services.RoomService;
import com.example.demo.services.RoomTableService;
import com.example.demo.services.TimeTableService;
import com.example.demo.services.TimingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("dev")
public class RoomTableServiceTest {
    @Autowired
    private RoomTableService roomTableService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private TimingService timingService;

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateRoomTablesFromRoom(){
        TimeTable timeTable = timeTableService.createTimeTable(Semester.SS, 2024);
        Room room = roomService.loadRoomByID("HS A");
        RoomTable roomTable = roomTableService.createRoomTableFromRoom(timeTable, room);

        assertEquals(room, roomTable.getRoom());
        assertEquals(timeTable, roomTable.getTimeTable());
        assertNotNull(roomTable.getAvailabilityMatrix());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateAvailabilityMatrixWithEmptyConstraints(){
        RoomTable roomTable = roomTableService.loadRoomTableByID(-41);
        AvailabilityMatrix availabilityMatrix = new AvailabilityMatrix(roomTable);
        assertNotNull(availabilityMatrix);
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateAvailabilityMatrixWithOneTimingConstraint(){
        RoomTable roomTable = roomTableService.loadRoomTableByID(-3);
        assertEquals(1, roomTable.getTimingConstraints().size());
        AvailabilityMatrix availabilityMatrix = new AvailabilityMatrix(roomTable);
        assertNotNull(availabilityMatrix);
        long totalAvailableTime = TimingConstants.START_TIME.until(TimingConstants.END_TIME, ChronoUnit.MINUTES) * 5;
        long toSubtract = roomTable.getTimingConstraints().getFirst().getDuration();
        assertEquals(totalAvailableTime-toSubtract, availabilityMatrix.getTotal_available_time());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadRoomTablesOfTimeTable(){
        TimeTable timeTable = timeTableService.loadTimeTable(-1);
        List<RoomTable> roomTables = roomTableService.loadAllOfTimeTable(timeTable);

        assertNotNull(roomTables);
        assertEquals(5, roomTables.size());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDeleteRoomTableWithAssignedCoursesAndTimingConstraints(){
        RoomTable roomTable = roomTableService.loadRoomTableByID(-1);
        assertNotEquals(0, roomTable.getAssignedCourseSessions().size());
        assertNotEquals(0, roomTable.getTimingConstraints().size());

        roomTableService.deleteRoomTable(roomTable);

        assertThrows(EntityNotFoundException.class, () -> roomTableService.loadRoomTableByID(-1));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDeleteRoomTableWithOutAssignedCourses(){
        RoomTable roomTable = roomTableService.loadRoomTableByID(-2);
        assertEquals(0, roomTable.getAssignedCourseSessions().size());
        assertNotEquals(0, roomTable.getTimingConstraints().size());

        roomTableService.deleteRoomTable(roomTable);

        assertThrows(EntityNotFoundException.class, () -> roomTableService.loadRoomTableByID(-2));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testAddTimingConstraint(){
        RoomTable roomTable = roomTableService.loadRoomTableByID(-4);

        Timing timingConstraint = timingService.createTiming(LocalTime.of(9,0),
                LocalTime.of(10,0), Day.MONDAY, TimingType.BLOCKED);

        System.out.println(roomTable.getAvailabilityMatrix());

        roomTableService.addTimingConstraints(roomTable, List.of(timingConstraint));

        System.out.println(roomTable.getAvailabilityMatrix());
    }
}
