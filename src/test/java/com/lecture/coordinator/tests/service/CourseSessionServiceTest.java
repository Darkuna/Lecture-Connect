package com.lecture.coordinator.tests.service;

import com.lecture.coordinator.model.Course;
import com.lecture.coordinator.model.CourseSession;
import com.lecture.coordinator.services.CourseService;
import com.lecture.coordinator.services.CourseSessionService;
import org.junit.jupiter.api.DisplayName;
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
public class CourseSessionServiceTest {
    @Autowired
    private CourseSessionService courseSessionService;
    @Autowired
    private CourseService courseService;

    @Test
    @DisplayName("Tests the creation of courseSessions for a course without groups that is not split")
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseSessionOfNormalCourse(){
        Course normalCourse = courseService.loadCourseById("703003");
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
        //TODO: create test for assigning a courseSession to a specific roomTable at a specific timing
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUnassignCourse(){
        //TODO: create a test for unassigning a courseSession
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadAllAssignedToRoomTable(){
        //TODO: create a test for loading all courseSessions assigned to a specific room
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadAllUnassignedCourseSessionsOfTimeTable(){
        //TODO: create a test for loading all unassigned course sessions for a specific timeTable
    }
}
