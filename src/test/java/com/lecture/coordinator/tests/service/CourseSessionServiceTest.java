package com.lecture.coordinator.tests.service;

import com.lecture.coordinator.exceptions.courseSession.CourseSessionNotAssignedException;
import com.lecture.coordinator.model.*;
import com.lecture.coordinator.model.enums.Day;
import com.lecture.coordinator.services.CourseService;
import com.lecture.coordinator.services.CourseSessionService;
import com.lecture.coordinator.services.RoomTableService;
import com.lecture.coordinator.services.TimingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityNotFoundException;
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
    private TimingService timingService;

    @Test
    @DisplayName("Tests the creation of courseSessions for a course without groups that is not split")
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseSessionOfNormalCourse(){
        Course normalCourse = courseService.loadCourseById("703003");
        normalCourse.setNumberOfGroups(1);
        normalCourse.setSplit(false);
        normalCourse.setSplitTimes(null);

        List<CourseSession> courseSessions = courseSessionService.createCourseSessionsFromCourse(normalCourse);

        assertEquals(1, courseSessions.size());

        CourseSession courseSession = courseSessions.get(0);

        assertEquals(normalCourse, courseSession.getCourse());
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

        List<CourseSession> courseSessions = courseSessionService.createCourseSessionsFromCourse(splitCourse);

        assertEquals(2, courseSessions.size());

        CourseSession courseSession1 = courseSessions.get(0);
        CourseSession courseSession2 = courseSessions.get(1);

        assertEquals(splitCourse, courseSession1.getCourse());
        assertEquals(120, courseSession1.getDuration());
        assertEquals(splitCourse.getTimingConstraints(), courseSession1.getTimingConstraints());

        assertEquals(splitCourse, courseSession2.getCourse());
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

        List<CourseSession> courseSessions = courseSessionService.createCourseSessionsFromCourse(groupCourse);

        assertEquals(6, courseSessions.size());

        CourseSession courseSession = courseSessions.get(0);

        assertEquals(groupCourse, courseSession.getCourse());
        assertEquals(groupCourse.getDuration(), courseSession.getDuration());
        assertEquals(groupCourse.getTimingConstraints(), courseSession.getTimingConstraints());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testAssignCourseSessionToRoomTable(){
        CourseSession courseSession = courseSessionService.loadCourseSessionByID(-2);
        RoomTable roomTable = roomTableService.loadRoomTableByID(-2);
        Timing timing = timingService.createTiming(LocalTime.of(8,0), LocalTime.of(10,0),
                Day.TUESDAY);
        courseSessionService.assignCourseSessionToRoomTable(courseSession, roomTable, timing);

        assertEquals(Day.TUESDAY, courseSession.getTiming().getDay());
        assertEquals(LocalTime.of(8,0), courseSession.getTiming().getStartTime());
        assertEquals(LocalTime.of(10,0), courseSession.getTiming().getEndTime());
        assertEquals(roomTable, courseSession.getRoomTable());
        assertTrue(courseSession.isAssigned());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUnassignCourse(){
        CourseSession courseSession = courseSessionService.loadCourseSessionByID(-6);
        assertTrue(courseSession.isAssigned());

        courseSessionService.unassignCourseSession(courseSession);

        assertFalse(courseSession.isAssigned());
        assertNull(courseSession.getRoomTable());
        assertNull(courseSession.getTiming());

    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadAllAssignedToRoomTable(){
        RoomTable roomTable = roomTableService.loadRoomTableByID(-1);
        List<CourseSession> courseSessions = courseSessionService.loadAllAssignedToRoomTable(roomTable);
        System.out.println(courseSessions);
        assertNotNull(courseSessions);
        assertEquals(1, courseSessions.size());
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
        CourseSession courseSession = courseSessionService.loadCourseSessionByID(-1);
        assertFalse(courseSession.isAssigned());

        courseSessionService.deleteCourseSession(courseSession);

        assertThrows(EntityNotFoundException.class, () -> courseSessionService.loadCourseSessionByID(-1));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testFixAssignedCourseSession() throws CourseSessionNotAssignedException {
        CourseSession courseSession = courseSessionService.loadCourseSessionByID(-6);
        assertTrue(courseSession.isAssigned());

        courseSessionService.fixCourseSession(courseSession);

        assertTrue(courseSession.isFixed());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testFixUnassignedCourseSession() {
        CourseSession courseSession = courseSessionService.loadCourseSessionByID(-1);
        assertFalse(courseSession.isAssigned());

        assertThrows(CourseSessionNotAssignedException.class, () -> courseSessionService.fixCourseSession(courseSession));
    }
}
