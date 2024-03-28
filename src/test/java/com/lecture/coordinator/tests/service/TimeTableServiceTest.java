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
import java.util.ArrayList;

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
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateTimeTableWithOneRoomAndOneCourseWithoutGroups(){
        List<Room> rooms = List.of(roomService.loadRoomByID("HS A"));
        List<Course> courses = List.of(courseService.loadCourseById("703003"));
        TimeTable timeTable = timeTableService.createTimeTable(Semester.SS, 2024, rooms, courses);

        assertEquals(rooms, timeTable.getRooms());
        assertEquals(courses, timeTable.getCourses());
        assertEquals(Semester.SS, timeTable.getSemester());
        assertEquals(2024, timeTable.getYear());

        //Tests for created roomTables
        List<RoomTable> roomTables = timeTable.getRoomTables();

        assertEquals(rooms.size(), roomTables.size());

        //Tests for created courseSessions
        List<CourseSession> courseSessions = timeTable.getCourseSessions();

        assertEquals(1, courseSessions.size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testAddRoomToTimeTable(){
        int numberOfRooms = 1;
        List<Room> rooms = new ArrayList<>(List.of(roomService.loadRoomByID("HS A")));
        List<Course> courses = new ArrayList<>(List.of(courseService.loadCourseById("703003")));
        TimeTable timeTable = timeTableService.createTimeTable(Semester.SS, 2024, rooms, courses);

        assertEquals(numberOfRooms, timeTable.getRooms().size());
        assertEquals(rooms.size(), timeTable.getRoomTables().size());

        Room roomToAdd = roomService.loadRoomByID("Rechnerraum 22");
        timeTableService.addRoom(timeTable, roomToAdd);

        assertEquals(numberOfRooms+1, timeTable.getRooms().size());
        assertEquals(numberOfRooms+1, timeTable.getRoomTables().size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testAddCourseToTimeTable(){
        int numberOfCourseSessions = 6;
        List<Room> rooms = new ArrayList<>(List.of(roomService.loadRoomByID("HS A")));
        List<Course> courses = new ArrayList<>(List.of(courseService.loadCourseById("703004")));
        TimeTable timeTable = timeTableService.createTimeTable(Semester.SS, 2024, rooms, courses);

        assertEquals(1, timeTable.getCourses().size());
        assertEquals(numberOfCourseSessions, timeTable.getCourseSessions().size());

        Course courseToAdd = courseService.loadCourseById("703003");
        timeTableService.addCourse(timeTable, courseToAdd);

        assertEquals(2, timeTable.getCourses().size());
        assertEquals(numberOfCourseSessions+1, timeTable.getCourseSessions().size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testRemoveRoomFromTimeTable(){
        //TODO: create a test for removing a room to the timeTable
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testRemoveCourseFromTimeTable(){
        //TODO: create a test for removing a course to the timeTable
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
