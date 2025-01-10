package at.uibk.leco.service;

import at.uibk.leco.models.*;
import at.uibk.leco.models.enums.Day;
import at.uibk.leco.services.CourseService;
import at.uibk.leco.services.CourseSessionService;
import at.uibk.leco.services.RoomTableService;
import at.uibk.leco.services.TimeTableService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("dev")
public class CourseSessionServiceTest {
    @Autowired
    private CourseSessionService courseSessionService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private RoomTableService roomTableService;
    @Autowired
    private TimeTableService timeTableService;

    @Test
    @DisplayName("Tests the creation of courseSessions for a course without groups that is not split")
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseSessionOfNormalCourse(){
        Course normalCourse = courseService.loadCourseById("703003");
        normalCourse.setNumberOfGroups(1);
        normalCourse.setSplit(false);
        normalCourse.setSplitTimes(null);

        List<CourseSession> courseSessions = courseSessionService.createCourseSessionsFromCourse(null,normalCourse);

        assertEquals(1, courseSessions.size());

        CourseSession courseSession = courseSessions.getFirst();

        assertEquals(normalCourse.getId(), courseSession.getCourseId());
        assertEquals(normalCourse.getDuration(), courseSession.getDuration());
        assertEquals(normalCourse.getTimingConstraints(), courseSession.getTimingConstraints());
    }

    @Test
    @DisplayName("Tests the creation of courseSessions for a course without groups that is split")
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseSessionOfSplitCourse(){
        Course splitCourse = courseService.loadCourseById("703013");
        splitCourse.setNumberOfGroups(1);
        splitCourse.setSplit(true);
        splitCourse.setSplitTimes(List.of(120,60));

        List<CourseSession> courseSessions = courseSessionService.createCourseSessionsFromCourse(null, splitCourse);

        assertEquals(2, courseSessions.size());

        CourseSession courseSession1 = courseSessions.get(0);
        CourseSession courseSession2 = courseSessions.get(1);

        assertEquals(splitCourse.getId(), courseSession1.getCourseId());
        assertEquals(120, courseSession1.getDuration());
        assertEquals(splitCourse.getTimingConstraints(), courseSession1.getTimingConstraints());

        assertEquals(splitCourse.getId(), courseSession2.getCourseId());
        assertEquals(60, courseSession2.getDuration());
        assertEquals(splitCourse.getTimingConstraints(), courseSession2.getTimingConstraints());
    }

    @Test
    @DisplayName("Tests the creation of courseSessions for a course with groups that is not split")
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseSessionOfCourseWithGroups(){
        Course groupCourse = courseService.loadCourseById("703004");
        groupCourse.setNumberOfGroups(6);
        groupCourse.setSplit(false);
        groupCourse.setSplitTimes(null);

        List<CourseSession> courseSessions = courseSessionService.createCourseSessionsFromCourse(null, groupCourse);

        assertEquals(6, courseSessions.size());

        CourseSession courseSession = courseSessions.getFirst();

        assertEquals(groupCourse.getId(), courseSession.getCourseId());
        assertEquals(groupCourse.getDuration(), courseSession.getDuration());
        assertEquals(groupCourse.getTimingConstraints(), courseSession.getTimingConstraints());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadAllAssignedToRoomTable(){
        RoomTable roomTable = roomTableService.loadRoomTableByID(-40);
        List<CourseSession> courseSessions = courseSessionService.loadAllAssignedToRoomTable(roomTable);
        System.out.println(courseSessions);
        assertNotNull(courseSessions);
        assertEquals(19, courseSessions.size());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDeleteAssignedCourseSession(){
        CourseSession courseSession = courseSessionService.loadCourseSessionByID(-6);
        assertTrue(courseSession.isAssigned());

        courseSessionService.deleteCourseSession(courseSession);

        assertThrows(EntityNotFoundException.class, () -> courseSessionService.loadCourseSessionByID(-6));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDeleteUnassignedCourseSession(){
        CourseSession courseSession = courseSessionService.loadCourseSessionByID(-420);
        assertFalse(courseSession.isAssigned());

        courseSessionService.deleteCourseSession(courseSession);

        assertThrows(EntityNotFoundException.class, () -> courseSessionService.loadCourseSessionByID(-420));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUnassignCourseSessions(){
        RoomTable roomTableWithAssignedCourseSessions = roomTableService.loadRoomTableByID(-41);
        List<CourseSession> courseSessions = roomTableWithAssignedCourseSessions.getAssignedCourseSessions();

        assertFalse(courseSessions.isEmpty());

        courseSessions = courseSessionService.unassignCourseSessions(courseSessions);

        for(CourseSession courseSession : courseSessions){
            assertFalse(courseSession.isAssigned());
        }
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadAllFromTimeTable(){
        int expected = 22;
        TimeTable timeTable = timeTableService.loadTimeTable(-1);

        List<CourseSession> courseSessions = courseSessionService.loadAllFromTimeTable(timeTable);

        assertEquals(expected, courseSessions.size());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUpdateCourseSessionThatWasUnassigned(){
        TimeTable timeTable = timeTableService.loadTimeTable(-3);
        CourseSession original = courseSessionService.loadCourseSessionByID(-1);
        CourseSession updated = courseSessionService.copyCourseSession(original);
        updated.setTiming(null);
        updated.setRoomTable(null);
        updated.setAssigned(false);

        courseSessionService.updateCourseSession(timeTable,updated,original);
        original = courseSessionService.loadCourseSessionByID(-1);

        assertEquals(original.getTiming(), updated.getTiming());
        assertEquals(original.getRoomTable(), updated.getRoomTable());
        assertEquals(original.isAssigned(), updated.isAssigned());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUpdateCourseSessionThatWasAssigned(){
        TimeTable timeTable = timeTableService.loadTimeTable(-1);
        CourseSession original = courseSessionService.loadCourseSessionByID(-430);
        CourseSession updated = courseSessionService.copyCourseSession(original);
        Timing timing = new Timing();
        timing.setDay(Day.MONDAY);
        timing.setStartTime(LocalTime.of(10,0));
        timing.setEndTime(LocalTime.of(11,0));
        updated.setAssigned(true);
        updated.setTiming(timing);
        updated.setRoomTable(timeTable.getRoomTables().getFirst());

        courseSessionService.updateCourseSession(timeTable,updated,original);
        original = courseSessionService.loadCourseSessionByID(-430);

        assertEquals(original.isAssigned(), updated.isAssigned());
        assertEquals(original.getTiming().getStartTime(), updated.getTiming().getStartTime());
        assertEquals(original.getTiming().getEndTime(), updated.getTiming().getEndTime());
        assertEquals(original.getTiming().getDay(), updated.getTiming().getDay());
        assertEquals(original.getRoomTable(), updated.getRoomTable());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUpdateCourseSessionThatWasMoved(){
        TimeTable timeTable = timeTableService.loadTimeTable(-3);
        CourseSession original = courseSessionService.loadCourseSessionByID(-1);
        CourseSession updated = courseSessionService.copyCourseSession(original);
        Timing timing = new Timing();
        timing.setDay(Day.MONDAY);
        timing.setStartTime(LocalTime.of(19,0));
        timing.setEndTime(LocalTime.of(20,0));
        updated.setTiming(timing);

        courseSessionService.updateCourseSession(timeTable,updated,original);
        original = courseSessionService.loadCourseSessionByID(-1);

        assertEquals(original.getTiming().getDay(), updated.getTiming().getDay());
        assertEquals(original.getTiming().getStartTime(), updated.getTiming().getStartTime());
        assertEquals(original.getTiming().getEndTime(), updated.getTiming().getEndTime());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUpdateCourseSessionThatWasFixed(){
        TimeTable timeTable = timeTableService.loadTimeTable(-3);
        CourseSession original = courseSessionService.loadCourseSessionByID(-1);
        CourseSession updated = courseSessionService.copyCourseSession(original);
        updated.setFixed(true);

        courseSessionService.updateCourseSession(timeTable,updated,original);
        original = courseSessionService.loadCourseSessionByID(-1);

        assertEquals(original.isFixed(), updated.isFixed());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUpdateCourseSessionWithNoChanges(){
        TimeTable timeTable = timeTableService.loadTimeTable(-1);
        CourseSession original = courseSessionService.loadCourseSessionByID(-430);
        CourseSession updated = courseSessionService.copyCourseSession(original);

        courseSessionService.updateCourseSession(timeTable,updated,original);
        original = courseSessionService.loadCourseSessionByID(-430);

        assertEquals(original.getRoomTable(), updated.getRoomTable());
        assertEquals(original.isAssigned(), updated.isAssigned());

    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseSession(){
        TimeTable timeTable = timeTableService.loadTimeTable(-1);
        int numberOfCourseSession = timeTable.getCourseSessions().size();
        CourseSession courseSession = courseSessionService.loadCourseSessionByID(-430);

        courseSessionService.createCourseSession(courseSession,timeTable);
        timeTable = timeTableService.loadTimeTable(-1);

        assertEquals(numberOfCourseSession + 1, timeTable.getCourseSessions().size());
    }
}