package com.lecture.coordinator.tests.service;

import com.lecture.coordinator.model.*;
import com.lecture.coordinator.services.CourseService;
import com.lecture.coordinator.services.RoomService;
import com.lecture.coordinator.services.TimeTableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
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
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateTimeTable(){
        List<Room> rooms = List.of(roomService.loadRoomByID("HS A"));
        List<Course> courses = List.of(courseService.loadCourseById("703003"));
        TimeTable timeTable = timeTableService.createTimeTable(Semester.SS, 2024, rooms, courses);

        assertEquals(rooms, timeTable.getRooms());
        assertEquals(courses, timeTable.getCourses());
        assertEquals(Semester.SS, timeTable.getSemester());
        assertEquals(2024, timeTable.getYear());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateRoomTablesOfTimeTable(){
        //TODO: create a test for creating roomTables of a given timeTable
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseSessions(){
        List<Room> rooms = List.of(roomService.loadRoomByID("HS A"));
        Course course1 = courseService.loadCourseById("703003");
        Course course2 = courseService.loadCourseById("703004");
        List<Course> courses = List.of(course1, course2);
        TimeTable timeTable = timeTableService.createTimeTable(Semester.SS, 2024, rooms, courses);

        timeTableService.createCourseSessions(timeTable);

        assertEquals(7, timeTable.getCourseSessions().size());
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
