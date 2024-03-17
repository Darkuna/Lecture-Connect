package com.lecture.coordinator.tests.service;

import com.lecture.coordinator.services.RoomTableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
public class RoomTableServiceTest {
    @Autowired
    private RoomTableService roomTableService;

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateRoomTablesFromRoom(){
        //TODO: create a test for creating a roomtable of a given room
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadAllAssignedCoursesForRoomTable(){
        //TODO: create a test that loads all courseSessions that are assigned to the given roomTable
    }
}
