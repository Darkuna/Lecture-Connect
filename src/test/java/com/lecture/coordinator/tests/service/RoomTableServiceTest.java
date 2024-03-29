package com.lecture.coordinator.tests.service;

import com.lecture.coordinator.model.*;
import com.lecture.coordinator.services.RoomService;
import com.lecture.coordinator.services.RoomTableService;
import com.lecture.coordinator.services.TimeTableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

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
        TimeTable timeTable = timeTableService.createTimeTable(Semester.SS, 2024, List.of(), List.of());
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
        System.out.println(availabilityMatrix);
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateAvailabilityMatrixWithOneTimingConstraint(){
        Timing timingConstraint = new Timing();
        timingConstraint.setDay(Day.MONDAY);
        timingConstraint.setStartTime(LocalTime.of(8,0));
        timingConstraint.setEndTime(LocalTime.of(18,0));
        AvailabilityMatrix availabilityMatrix = new AvailabilityMatrix(List.of(timingConstraint));
        System.out.println(availabilityMatrix);
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadRoomTablesOfTimeTable(){
        TimeTable timeTable = timeTableService.loadAllTimeTables().get(0);
        List<RoomTable> roomTables = roomTableService.loadRoomTablesOfTimeTable(timeTable);

        assertNotNull(roomTables);
        assertEquals(5, roomTables.size());
    }
}
