package com.example.demo.controllers;

import com.example.demo.dto.RoomDTO;
import com.example.demo.models.Room;
import com.example.demo.services.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoomController.class)
@ActiveProfiles("dev")
public class RoomControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RoomService roomService;

    @Test
    public void testGetRooms() throws Exception{
        List<Room> rooms = List.of(new Room(), new Room(), new Room());
        when(roomService.loadAllRooms()).thenReturn(rooms);

        mockMvc.perform(get("/api/rooms"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateRoom() throws Exception {
        RoomDTO roomDto = new RoomDTO();
        roomDto.setCapacity(100);
        roomDto.setComputersAvailable(true);

        Room room = new Room();
        room.setCapacity(100);
        room.setComputersAvailable(true);

        when(roomService.createRoom(any(Room.class))).thenReturn(room);

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(roomDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateRoom() throws Exception {
        String roomId = "1";
        RoomDTO roomDto = new RoomDTO();
        roomDto.setCapacity(200);
        roomDto.setComputersAvailable(false);

        Room existingRoom = new Room();
        existingRoom.setId(roomId);
        existingRoom.setCapacity(100);
        existingRoom.setComputersAvailable(true);

        when(roomService.loadRoomByID(roomId)).thenReturn(existingRoom);
        when(roomService.updateRoom(any(Room.class), anyInt(), anyBoolean())).thenReturn(existingRoom);

        mockMvc.perform(put("/api/rooms/{id}", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(roomDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteRoom() throws Exception {
        String roomId = "1";

        doNothing().when(roomService).deleteRoom(any(Room.class));

        mockMvc.perform(delete("/api/rooms/{id}", roomId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteMultipleRooms() throws Exception {
        List<String> roomIds = List.of("1", "2", "3");

        doNothing().when(roomService).deleteMultipleRooms(roomIds);

        mockMvc.perform(delete("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(roomIds)))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void testGetRoomById() throws Exception {
        String roomId = "1";
        Room room = new Room();
        room.setId(roomId);
        room.setCapacity(100);
        room.setCreatedAt(LocalDateTime.now());
        room.setUpdatedAt(LocalDateTime.now());
        room.setComputersAvailable(true);

        when(roomService.loadRoomByID(roomId)).thenReturn(room);

        mockMvc.perform(get("/api/rooms/{id}", roomId))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
