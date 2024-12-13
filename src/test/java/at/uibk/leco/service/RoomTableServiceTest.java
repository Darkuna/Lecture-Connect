package at.uibk.leco.service;

import at.uibk.leco.models.Room;
import at.uibk.leco.models.RoomTable;
import at.uibk.leco.models.TimeTable;
import at.uibk.leco.models.enums.Semester;
import at.uibk.leco.scheduling.AvailabilityMatrix;
import at.uibk.leco.services.RoomService;
import at.uibk.leco.services.RoomTableService;
import at.uibk.leco.services.TimeTableService;
import at.uibk.leco.services.TimingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import jakarta.persistence.EntityNotFoundException;

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

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadRoomTableById(){
        RoomTable roomTable = roomTableService.loadRoomTableByID(-1);
        assertNotNull(roomTable);
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadRoomTableWithWrongId(){
        assertThrows(EntityNotFoundException.class, () -> roomTableService.loadRoomTableByID(-2000));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadRoomTablesOfTimeTable(){
        TimeTable timeTable = timeTableService.loadTimeTable(-1);
        List<RoomTable> roomTables = roomTableService.loadAllOfTimeTable(timeTable);

        assertNotNull(roomTables);
        assertEquals(2, roomTables.size());
    }
}
