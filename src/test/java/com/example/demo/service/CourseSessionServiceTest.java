package com.example.demo.service;

import at.uibk.leco.models.Course;
import at.uibk.leco.models.CourseSession;
import at.uibk.leco.models.RoomTable;
import at.uibk.leco.services.CourseService;
import at.uibk.leco.services.CourseSessionService;
import at.uibk.leco.services.RoomTableService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import jakarta.persistence.EntityNotFoundException;
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
        assertEquals(41, courseSessions.size());
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
}