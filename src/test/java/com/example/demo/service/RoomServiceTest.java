package com.example.demo.service;

import com.example.demo.models.Room;
import com.example.demo.services.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import jakarta.persistence.EntityNotFoundException;
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
    @DirtiesContext
    public void testUpdateRoom(){
        String id = "HSB 3";
        Room room = roomService.loadRoomByID(id);
        room = roomService.updateRoom(room, 40, true);

        assertEquals(40, room.getCapacity());
        assertTrue(room.isComputersAvailable());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDeleteRoom(){
        String id = "HSB 3";
        Room room = roomService.loadRoomByID(id);

        roomService.deleteRoom(room);

        assertThrows(EntityNotFoundException.class, ()->roomService.loadRoomByID(id));
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
    @WithMockUser(username = "user1", authorities = {"USER"})
    @DirtiesContext
    public void testDeleteMultipleRooms() {
        List<String> roomsToBeDeleted = List.of("Rechnerraum 20","Rechnerraum 21");

        roomService.deleteMultipleRooms(roomsToBeDeleted);

        assertThrows(EntityNotFoundException.class, () -> roomService.loadRoomByID("Rechnerraum 20"));
        assertThrows(EntityNotFoundException.class, () -> roomService.loadRoomByID("Rechnerraum 21"));
    }
}
