package com.example.demo.service;

import com.example.demo.dto.TimeTableNameDTO;
import com.example.demo.models.*;
import com.example.demo.models.enums.CourseType;
import com.example.demo.models.enums.Semester;
import com.example.demo.services.CourseService;
import com.example.demo.services.RoomService;
import com.example.demo.services.TimeTableService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Random;

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

        assertEquals(3, timeTables.size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimeTableNames(){
        List<TimeTableNameDTO> timeTableNames = timeTableService.loadTimeTableNames();
        assertEquals(3, timeTableNames.size());
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

    /**
     * Test is only used for test data creation
     */
    @Test
    @DirtiesContext
    @Disabled
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void createTestData(){
        TimeTable timeTable = timeTableService.loadTimeTable(-2);
        List<Course> courses = courseService.loadAllCourses();
        Random random = new Random();
        for(Course course : courses){
            if(course.getCourseType().equals(CourseType.PS)){
                course.setNumberOfGroups(random.nextInt(3,9));
            }
            timeTableService.addCourseSessions(timeTable, course);
        }
        for(CourseSession courseSession : timeTable.getCourseSessions()){
            System.out.println(courseSession);
        }
    }
}
