package com.lecture.coordinator.tests.service;

import com.lecture.coordinator.model.*;
import com.lecture.coordinator.services.CourseService;
import com.lecture.coordinator.services.RoomService;
import com.lecture.coordinator.services.TimeTableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("dev")
public class TimeTableServiceTest {
    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private CourseService courseService;

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateTimeTableWithOneRoomAndOneCourseWithoutGroups(){
        TimeTable timeTable = timeTableService.createTimeTable(Semester.SS, 2024);

        assertEquals(Semester.SS, timeTable.getSemester());
        assertEquals(2024, timeTable.getYear());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testAddRoomTableToTimeTable(){
        TimeTable timeTable = timeTableService.createTimeTable(Semester.SS, 2024);
        Room room = roomService.loadRoomByID("HS A");
        RoomTable roomTable = timeTableService.addRoomTable(timeTable, room);

        assertEquals(room, roomTable.getRoom());
        assertEquals(timeTable, roomTable.getTimeTable());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testAddCourseSessionToTimeTable(){
        TimeTable timeTable = timeTableService.createTimeTable(Semester.SS, 2024);
        Course course = courseService.loadCourseById("703003");
        course.setNumberOfGroups(6);
        course.setSplit(false);
        course.setSplitTimes(null);
        List<CourseSession> courseSessions = timeTableService.addCourseSessions(timeTable, course);

        assertEquals(course.getNumberOfGroups(), courseSessions.size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testRemoveRoomTableFromTimeTable(){
        //TODO: create a test for removing a roomTable from a timeTable
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testRemoveCourseSessionFromTimeTable(){
        //TODO: create a test for removing a courseSession from a timeTable
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimeTables(){
        List<TimeTable> timeTables = timeTableService.loadAllTimeTables();

        assertEquals(1, timeTables.size());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimeTable(){
        TimeTable timeTable = timeTableService.loadTimeTable(-1);

        assertEquals(Semester.SS, timeTable.getSemester());
        assertEquals(2023, timeTable.getYear());
        assertNotNull(timeTable.getRoomTables());
        assertNotNull(timeTable.getCourseSessions());
    }

}
