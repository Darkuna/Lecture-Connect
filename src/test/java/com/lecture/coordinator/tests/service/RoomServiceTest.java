package com.lecture.coordinator.tests.service;

import com.lecture.coordinator.model.Day;
import com.lecture.coordinator.model.Room;
import com.lecture.coordinator.model.Timing;
import com.lecture.coordinator.services.RoomService;
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
public class RoomServiceTest {
    @Autowired
    private RoomService roomService;


    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateRoom(){
        Timing constraint = new Timing();
        constraint.setDay(Day.MONDAY);
        constraint.setStartTime(LocalTime.of(12,0));
        constraint.setEndTime(LocalTime.of(14,30));
        List<Timing> timingConstraints = List.of(constraint);
        Room newRoom = roomService.createRoom("RR24", 25, true, timingConstraints);

        assertEquals("RR24", newRoom.getId());
        assertEquals(25, newRoom.getCapacity());
        assertTrue(newRoom.isComputersAvailable());
        assertEquals(timingConstraints, newRoom.getTimingConstraints());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUpdateRoom(){
        //TODO: create test for updating a room
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDeleteRoom(){
        //TODO: create test for deleting a room
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
}
