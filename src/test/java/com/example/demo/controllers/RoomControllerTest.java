package com.example.demo.controllers;

import com.example.demo.models.Room;
import com.example.demo.services.JwtService;
import com.example.demo.services.RoomService;
import com.example.demo.services.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ActiveProfiles("dev")
public class RoomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserInfoService userInfoService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private RoomService roomService;

    @Test
    public void testGetRooms() throws Exception{
        List<Room> rooms = List.of(new Room(), new Room(), new Room());
        when(roomService.loadAllRooms()).thenReturn(rooms);

        mockMvc.perform(get("/api/rooms"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }
}
