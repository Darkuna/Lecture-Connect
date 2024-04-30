package com.example.demo.service;

import com.example.demo.models.*;
import com.example.demo.models.enums.Day;
import com.example.demo.models.enums.Semester;
import com.example.demo.services.RoomService;
import com.example.demo.services.RoomTableService;
import com.example.demo.services.TimeTableService;
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
@WebAppConfiguration
@ActiveProfiles("dev")
public class RoomTableServiceTest {
    @Autowired
    private RoomTableService roomTableService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private TimeTableService timeTableService;

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
        AvailabilityMatrix availabilityMatrix = new AvailabilityMatrix(null);
        assertNotNull(availabilityMatrix);
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateAvailabilityMatrixWithOneTimingConstraint(){
        Timing timingConstraint = new Timing();
        timingConstraint.setDay(Day.MONDAY);
        timingConstraint.setStartTime(LocalTime.of(8,0));
        timingConstraint.setEndTime(LocalTime.of(18,0));
        AvailabilityMatrix availabilityMatrix = new AvailabilityMatrix(List.of(timingConstraint));
        assertNotNull(availabilityMatrix);
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadRoomTablesOfTimeTable(){
        TimeTable timeTable = timeTableService.loadAllTimeTables().get(0);
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
}
