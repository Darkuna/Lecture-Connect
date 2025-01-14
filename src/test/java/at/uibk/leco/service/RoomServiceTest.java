package at.uibk.leco.service;

import at.uibk.leco.models.Room;
import at.uibk.leco.models.Timing;
import at.uibk.leco.models.TimeTable;
import at.uibk.leco.models.enums.Day;
import at.uibk.leco.models.enums.TimingType;
import at.uibk.leco.services.RoomService;
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

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("dev")
public class RoomServiceTest {
    @Autowired
    private RoomService roomService;
    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private TimingService timingService;

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateRoom(){
        Room newRoom = roomService.createRoom("RR24", 25, true);

        assertEquals("RR24", newRoom.getId());
        assertEquals(25, newRoom.getCapacity());
        assertTrue(newRoom.isComputersAvailable());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateRoomFromRoomObject(){
        Room room = new Room();
        Timing timing = timingService.createTiming(LocalTime.of(10,0), LocalTime.of(12,9),
                Day.MONDAY, TimingType.BLOCKED);
        room.setCapacity(25);
        room.setComputersAvailable(true);
        room.setId("TestRoom");
        room.setTimingConstraints(List.of(timing));

        roomService.createRoom(room);

        assertNotNull(roomService.loadRoomByID("TestRoom"));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    @DirtiesContext
    public void testUpdateRoom(){
        String id = "HSB 4";
        Room room = roomService.loadRoomByID(id);
        room = roomService.updateRoom(room, 40, true);

        assertEquals(40, room.getCapacity());
        assertTrue(room.isComputersAvailable());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDeleteRoom(){
        String id = "HSB 4";
        Room room = roomService.loadRoomByID(id);

        roomService.deleteRoom(room);

        assertThrows(EntityNotFoundException.class, ()->roomService.loadRoomByID(id));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadRoomByID(){
        String id = "HSB 4";
        Room room = roomService.loadRoomByID(id);

        assertEquals(id, room.getId());
        assertEquals(48, room.getCapacity());
        assertFalse(room.isComputersAvailable());

    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadAllRooms(){
        int numberOfRooms = 29;
        List<Room> rooms = roomService.loadAllRooms();

        assertEquals(numberOfRooms, rooms.size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    @DirtiesContext
    public void testDeleteMultipleRooms() {
        List<String> roomsToBeDeleted = List.of("Rechnerraum 20","Rechnerraum 21");

        roomService.deleteMultipleRooms(roomsToBeDeleted);

        assertThrows(EntityNotFoundException.class, () -> roomService.loadRoomByID("Rechnerraum 20"));
        assertThrows(EntityNotFoundException.class, () -> roomService.loadRoomByID("Rechnerraum 21"));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadRoomsNotInTimeTable(){
        TimeTable timeTable = timeTableService.loadTimeTable(-5);
        int expected = roomService.loadAllRooms().size() - 2;
        List<Room> rooms = roomService.loadAllRoomsNotInTimeTable(timeTable);
        assertEquals(expected, rooms.size());
    }
}
