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
public class AssignmentAlgorithmTest {
    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private CourseService courseService;

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testAssignmentAlgorithm(){
        TimeTable timeTable = timeTableService.createTimeTable(Semester.SS, 2025);
        List<Room> rooms = roomService.loadAllRooms();
        List<Course> courses = courseService.loadAllCourses();
        Random random = new Random();

        for(Room room : rooms){
            timeTableService.addRoomTable(timeTable, room);
        }

        for(Course course : courses){
            if(course.getCourseType().equals(CourseType.PS)){
                course.setNumberOfGroups(random.nextInt(3,9));
            }
            timeTableService.addCourseSessions(timeTable, course);
        }

        timeTableService.assignCourseSessionsToRooms(timeTable);
    }
}