package at.uibk.leco.service;

import at.uibk.leco.dto.TimeTableNameDTO;
import at.uibk.leco.models.*;
import at.uibk.leco.models.enums.CourseType;
import at.uibk.leco.models.enums.Semester;
import at.uibk.leco.repositories.TimeTableRepository;
import at.uibk.leco.services.CourseService;
import at.uibk.leco.services.RoomService;
import at.uibk.leco.services.TimeTableService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
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
    @Autowired
    private TimeTableRepository timeTableRepository;

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateTimeTableWithOneRoomAndOneCourseWithoutGroups(){
        TimeTable timeTable = timeTableService.createTimeTable("Test", Semester.SS, 2024);

        assertEquals(Semester.SS, timeTable.getSemester());
        assertEquals(2024, timeTable.getYear());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateRoomTableToTimeTable(){
        TimeTable timeTable = timeTableService.createTimeTable("Test", Semester.SS, 2024);
        Room room = roomService.loadRoomByID("HS A");
        room.setTimingConstraints(new ArrayList<>());
        RoomTable roomTable = timeTableService.createRoomTable(timeTable, room);

        assertEquals(timeTable, roomTable.getTimeTable());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testAddCourseSessionToTimeTable(){
        TimeTable timeTable = timeTableService.createTimeTable("Test",Semester.SS, 2024);
        Course course = courseService.loadCourseById("703003");
        course.setNumberOfGroups(6);
        course.setSplit(false);
        course.setSplitTimes(null);
        List<CourseSession> courseSessions = timeTableService.createCourseSessions(timeTable, course);

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
    public void testLoadTimeTableNames(){
        int numberOfTimeTables = timeTableRepository.findAll().size();
        List<TimeTableNameDTO> timeTableNames = timeTableService.loadTimeTableNames();
        assertEquals(numberOfTimeTables, timeTableNames.size());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimeTable(){
        TimeTable timeTable = timeTableService.loadTimeTable(-1);

        assertEquals(Semester.WS, timeTable.getSemester());
        assertEquals(8888, timeTable.getYear());
        assertNotNull(timeTable.getRoomTables());
        assertNotNull(timeTable.getCourseSessions());
    }

    /**
     * Test was only used for test data creation
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
            timeTableService.createCourseSessions(timeTable, course);
        }
        for(CourseSession courseSession : timeTable.getCourseSessions()){
            System.out.println(courseSession);
        }
    }
}
