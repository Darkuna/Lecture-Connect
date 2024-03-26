package com.lecture.coordinator.tests.service;

import com.lecture.coordinator.model.Room;
import com.lecture.coordinator.model.RoomTable;
import com.lecture.coordinator.model.Semester;
import com.lecture.coordinator.model.TimeTable;
import com.lecture.coordinator.services.RoomService;
import com.lecture.coordinator.services.RoomTableService;
import com.lecture.coordinator.services.TimeTableService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

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
        //RoomTable roomTable = roomTableService.createRoomTableFromRoom(timeTable, room);
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadAllAssignedCoursesForRoomTable(){
        //TODO: create a test that loads all courseSessions that are assigned to the given roomTable
    }
}
