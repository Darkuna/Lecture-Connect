package com.example.demo.service;

import com.example.demo.constants.TimingConstants;
import com.example.demo.models.*;
import com.example.demo.models.enums.CourseType;
import com.example.demo.models.enums.Day;
import com.example.demo.models.enums.Semester;
import com.example.demo.models.enums.TimingType;
import com.example.demo.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("dev")
public class AssignmentAlgorithmTest {
    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomTableService roomTableService;
    @Autowired
    private CourseService courseService;


    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testAssignmentAlgorithm(){
        TimeTable timeTable = timeTableService.createTimeTable("Test-TimeTable", Semester.SS, 2025);
        List<Room> rooms = roomService.loadAllRooms();
        List<Course> courses = courseService.loadAllCourses();
        Random random = new Random();
        List<Timing> timingConstraints = new ArrayList<>();
        int firstRandomValue;
        int secondRandomValue;
        int thirdRandomValue;
        int start;

        // create roomTables and assign random timing constraints
        for(Room room : rooms){
            room.setTimingConstraints(new ArrayList<>());
            RoomTable roomTable = timeTableService.createRoomTable(timeTable, room);
            
            //Block two random days of the week
            firstRandomValue = random.nextInt(5);
            timingConstraints.add(new Timing(TimingConstants.START_TIME, TimingConstants.END_TIME, Day.values()[firstRandomValue], TimingType.BLOCKED));
            secondRandomValue = firstRandomValue;
            while(secondRandomValue == firstRandomValue){
                secondRandomValue = random.nextInt(5);
            }
            timingConstraints.add(new Timing(TimingConstants.START_TIME, TimingConstants.END_TIME, Day.values()[secondRandomValue], TimingType.BLOCKED));
            //Block another three random 2-hour-slots
            thirdRandomValue = firstRandomValue;
            while(thirdRandomValue == firstRandomValue || thirdRandomValue == secondRandomValue){
                thirdRandomValue = random.nextInt(5);
            }
            start = random.nextInt() % 2 == 0 ? 8 : 9;
            for(int i = 0; i < 3; i++){
                timingConstraints.add(new Timing(LocalTime.of(start,0), LocalTime.of(start+2, 0), Day.values()[thirdRandomValue], TimingType.BLOCKED));
                start += 4;
            }

            roomTableService.addTimingConstraints(roomTable,timingConstraints);
            timingConstraints.clear();

        }

        // create courseSessions and choose random number of groups for ps courses
        for(Course course : courses){
            if(course.getCourseType().equals(CourseType.PS)){
                course.setNumberOfGroups(random.nextInt(6,9));
            }
            timeTableService.createCourseSessions(timeTable, course);
        }

        // start algorithm
        boolean success = timeTableService.assignCourseSessionsToRooms(timeTable);

        //verify
        assertTrue(success);
    }


}