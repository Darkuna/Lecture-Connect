package com.example.demo.service;

import com.example.demo.models.*;
import com.example.demo.models.enums.Day;
import com.example.demo.models.enums.Semester;
import com.example.demo.models.enums.TimingType;
import com.example.demo.scheduling.AvailabilityMatrix;
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
import java.util.ArrayList;
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
    public void testCreateRoomTableFromRoom(){
        TimeTable timeTable = timeTableService.createTimeTable("Test-Table", Semester.SS, 2024);
        Room room = roomService.loadRoomByID("HS A");
        room.setTimingConstraints(new ArrayList<>());
        RoomTable roomTable = roomTableService.createRoomTableFromRoom(timeTable, room);

        assertEquals(timeTable, roomTable.getTimeTable());
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
    public void testCreateAvailabilityMatrixWithSevenTimingConstraint(){
        RoomTable roomTable = roomTableService.loadRoomTableByID(-90);
        assertEquals(7, roomTable.getTimingConstraints().size());
        AvailabilityMatrix availabilityMatrix = new AvailabilityMatrix(roomTable);
        assertNotNull(availabilityMatrix);
        assertEquals(360, availabilityMatrix.getTotalAvailableTime());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadRoomTablesOfTimeTable(){
        TimeTable timeTable = timeTableService.loadTimeTable(-1);
        List<RoomTable> roomTables = roomTableService.loadAllOfTimeTable(timeTable);

        assertNotNull(roomTables);
        assertEquals(2, roomTables.size());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDeleteRoomTableWithAssignedCourses(){
        RoomTable roomTable = roomTableService.loadRoomTableByID(-41);
        assertNotEquals(0, roomTable.getAssignedCourseSessions().size());

        roomTableService.deleteRoomTable(roomTable);

        assertThrows(EntityNotFoundException.class, () -> roomTableService.loadRoomTableByID(-41));
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
