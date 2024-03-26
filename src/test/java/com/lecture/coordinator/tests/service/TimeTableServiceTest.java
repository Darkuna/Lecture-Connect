package com.lecture.coordinator.tests.service;

import com.lecture.coordinator.services.TimeTableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("dev")
public class TimeTableServiceTest {
    @Autowired
    private TimeTableService timeTableService;

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateTimeTable(){
        //TODO: create a test for creating a timetable
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateRoomTablesOfTimeTable(){
        //TODO: create a test for creating roomTables of a given timeTable
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseSessions(){
        //TODO: create a test for creating courseSessions for all courses of the timetable
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testAddRoom(){
        //TODO: create a test for adding a room to the timeTable
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testAddCourse(){
        //TODO: create a test for adding a course to the timeTable
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testRemoveRoom(){
        //TODO: create a test for removing a room to the timeTable
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testRemoveCourse(){
        //TODO: create a test for removing a course to the timeTable
    }




}
