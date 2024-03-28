package com.lecture.coordinator.tests.service;

import com.lecture.coordinator.model.Day;
import com.lecture.coordinator.model.Room;
import com.lecture.coordinator.model.Timing;
import com.lecture.coordinator.services.RoomService;
import com.lecture.coordinator.services.TimingService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("dev")
public class RoomServiceTest {
    @Autowired
    private RoomService roomService;

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
    public void testUpdateRoom(){
        String id = "HSB 3";
        Room room = roomService.loadRoomByID(id);
        room = roomService.updateRoom(room, 40, true);

        assertEquals(40, room.getCapacity());
        assertEquals(true, room.isComputersAvailable());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    @DirtiesContext
    public void testDeleteRoom(){
        String id = "HSB 3";
        Room room = roomService.loadRoomByID(id);

        roomService.deleteRoom(room);

        room = roomService.loadRoomByID(id);
        assertNull(room);
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadRoomByID(){
        String id = "HSB 3";
        Room room = roomService.loadRoomByID(id);

        assertEquals(id, room.getId());
        assertEquals(30, room.getCapacity());
        assertFalse(room.isComputersAvailable());

    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadAllRooms(){
        int numberOfRooms = 20;
        List<Room> rooms = roomService.loadAllRooms();

        assertEquals(numberOfRooms, rooms.size());
    }

    @Test
    @Disabled
    @WithMockUser(username = "user1", authorities = {"USER"})
    @DirtiesContext
    public void testDeleteMultipleRooms() {
        Room room1 = roomService.loadRoomByID("Rechnerraum 20");
        Room room2 = roomService.loadRoomByID("Rechnerraum 21");
        List<Room> toBeDeleted = List.of(room1, room2);

        //TODO: Deleting rooms that are assigned to a timeTable is currently not possible. Think of a strategy for that.
        //data tables associated to that: ROOM_TABLE and TIME_TABLE_ROOMS
        roomService.deleteMultipleRooms(toBeDeleted);

        room1 = roomService.loadRoomByID("Rechnerraum 20");
        room2 = roomService.loadRoomByID("Rechnerraum 21");

        assertNull(room1);
        assertNull(room2);
    }
}
