package at.uibk.leco.service;

import at.uibk.leco.dto.CourseDTO;
import at.uibk.leco.dto.RoomDTO;
import at.uibk.leco.dto.TimeTableCreationDTO;
import at.uibk.leco.dto.TimeTableNameDTO;
import at.uibk.leco.models.*;
import at.uibk.leco.models.enums.CourseType;
import at.uibk.leco.models.enums.Semester;
import at.uibk.leco.repositories.TimeTableRepository;
import at.uibk.leco.scheduling.Candidate;
import at.uibk.leco.scheduling.CollisionType;
import at.uibk.leco.scheduling.Scheduler;
import at.uibk.leco.services.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateTimeTableFromDTO(){
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setCapacity(100);
        roomDTO.setId("HS A");
        roomDTO.setComputersAvailable(false);

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName("Einführung in die Programmierung");
        courseDTO.setId("703003");
        courseDTO.setSplitTimes(null);
        courseDTO.setCourseType("VO");
        courseDTO.setDuration(180);
        courseDTO.setComputersNecessary(false);
        courseDTO.setNumberOfGroups(0);
        courseDTO.setNumberOfParticipants(300);
        courseDTO.setSemester(1);
        courseDTO.setStudyType("BACHELOR_CS");

        TimeTableCreationDTO dto = new TimeTableCreationDTO();
        dto.setName("TestTimeTable");
        dto.setSemester("SS");
        dto.setYear(2024);
        dto.setStatus("NEW");
        dto.setCourses(List.of(courseDTO));
        dto.setRooms(List.of(roomDTO));

        TimeTable timeTable = timeTableService.createTimeTable(dto);

        assertEquals(1, timeTable.getRoomTables().size());
        assertEquals(1, timeTable.getCourseSessions().size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateRoomTableForTimeTable(){
        TimeTable timeTable = timeTableService.createTimeTable("Test", Semester.SS, 2024);
        Room room = roomService.loadRoomByID("HS A");
        room.setTimingConstraints(new ArrayList<>());
        RoomTable roomTable = timeTableService.createRoomTable(timeTable, room);

        assertEquals(timeTable, roomTable.getTimeTable());
        assertEquals(1, timeTable.getRoomTables().size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseSessionForTimeTable(){
        TimeTable timeTable = timeTableService.createTimeTable("Test",Semester.SS, 2024);
        Course course = courseService.loadCourseById("703003");
        course.setNumberOfGroups(6);
        course.setSplit(false);
        course.setSplitTimes(null);
        List<CourseSession> courseSessions = timeTableService.createCourseSessions(timeTable, course);

        assertEquals(course.getNumberOfGroups(), courseSessions.size());
        assertEquals(timeTable.getCourseSessions().size(), courseSessions.size());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testRemoveRoomTableFromTimeTable(){
        TimeTable timeTable = timeTableService.loadTimeTable(-1);
        int numberOfRoomTables = timeTable.getRoomTables().size();
        RoomTable roomTable = timeTable.getRoomTables().getFirst();
        timeTableService.removeRoomTable(timeTable, roomTable);

        timeTable = timeTableService.loadTimeTable(-1);
        assertEquals(numberOfRoomTables - 1, timeTable.getRoomTables().size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testRemoveCourseSessionFromTimeTable(){
        TimeTable timeTable = timeTableService.loadTimeTable(-1);
        int numberOfCourseSessions = timeTable.getCourseSessions().size();
        CourseSession courseSession = timeTable.getCourseSessions().getFirst();
        timeTableService.removeCourseSession(timeTable, courseSession);

        timeTable = timeTableService.loadTimeTable(-1);
        assertEquals(numberOfCourseSessions - 1, timeTable.getCourseSessions().size());
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
        assertEquals(2022, timeTable.getYear());
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

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDeleteTimeTable(){
        TimeTable timeTable = timeTableService.loadTimeTable(-5);
        timeTableService.deleteTimeTable(timeTable);

        assertThrows(EntityNotFoundException.class, () -> timeTableService.loadTimeTable(-5));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testAssignUnassignedCourseSessions(){
        TimeTable timeTable = timeTableService.loadTimeTable(-5);

        assertFalse(timeTable.getUnassignedCourseSessions().isEmpty());
        timeTableService.assignCourseSessionsToRooms(timeTable);
        assertTrue(timeTable.getUnassignedCourseSessions().isEmpty());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCollisionCheckWhereNoCollisionsAppear(){
        TimeTable timeTable = timeTableService.loadTimeTable(-5);

        assertFalse(timeTable.getUnassignedCourseSessions().isEmpty());
        Map<CourseSession, List<CollisionType>> collisions = timeTableService.checkCollisions(timeTable);
        assertTrue(collisions.isEmpty());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUnassignAllCourseSessions(){
        TimeTable timeTable = timeTableService.loadTimeTable(-3);

        assertFalse(timeTable.getAssignedCourseSessions().isEmpty());
        timeTableService.unassignAllCourseSessions(timeTable);
        assertTrue(timeTable.getAssignedCourseSessions().isEmpty());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUnassignAllCourseSessionsWithOneFixedCourseSession(){
        TimeTable timeTable = timeTableService.loadTimeTable(-3);
        //fix one of the assigned courseSessions
        timeTable.getAssignedCourseSessions().getFirst().setFixed(true);

        assertFalse(timeTable.getAssignedCourseSessions().isEmpty());
        timeTableService.unassignAllCourseSessions(timeTable);
        assertEquals(1, timeTable.getAssignedCourseSessions().size());
    }


    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUnassignCollisions() {
        TimeTable timeTable = timeTableService.loadTimeTable(-3);
        int numberOfCollisions = timeTableService.checkCollisions(timeTable).size();
        int assignedCourseSessions = timeTable.getAssignedCourseSessions().size();

        timeTableService.unassignCollisions(timeTable);

        assertEquals(assignedCourseSessions - numberOfCollisions, timeTable.getAssignedCourseSessions().size());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUpdateCourseSessions() {
        TimeTable timeTable = timeTableService.loadTimeTable(-2);
        List<CourseSession> courseSessions = timeTable.getCourseSessions();
        timeTableService.updateCourseSessions(timeTable, timeTable.getCourseSessions());

        assertEquals(courseSessions.size(), timeTable.getCourseSessions().size());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testFindCourseSessionByName() {
        TimeTable timeTable = timeTableService.loadTimeTable(-1);
        String courseSessionName = "PS Rechnerarchitektur - Group 10";
        CourseSession courseSession = timeTableService.findCourseSessionByName(timeTable, courseSessionName);

        assertNotNull(courseSession);
    }

}
