package com.example.demo.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetRoomsFromDatabase() throws Exception {
        mockMvc.perform(get("/api/rooms"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(28)));
    }

    @Test
    public void testGetRoomByID() throws Exception {
        mockMvc.perform(get("/api/rooms/HS A"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
