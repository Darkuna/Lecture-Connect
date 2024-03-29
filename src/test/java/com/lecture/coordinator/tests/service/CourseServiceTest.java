package com.lecture.coordinator.tests.service;

import com.lecture.coordinator.model.*;
import com.lecture.coordinator.model.enums.CourseType;
import com.lecture.coordinator.model.enums.Day;
import com.lecture.coordinator.services.CourseService;
import com.lecture.coordinator.services.TimingService;
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
public class CourseServiceTest {
    @Autowired
    private CourseService courseService;
    @Autowired
    private TimingService timingService;

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourse() {
        String id = "lv232325";
        String name = "TestCourse";
        CourseType courseType = CourseType.VO;
        String lecturer = "Johannes Karrer";
        int duration = 2;
        int semester = 1;
        int numberOfParticipants = 35;
        boolean computersNecessary = false;
        Timing constraint = timingService.createTiming(LocalTime.of(12,0), LocalTime.of(14,30), Day.MONDAY);
        List<Timing> timingConstraints = List.of(constraint);

        Course course = courseService.createCourse(id, name, courseType, lecturer, semester, duration, numberOfParticipants,
                computersNecessary, timingConstraints);

        assertEquals(id, course.getId());
        assertEquals(name, course.getName());
        assertEquals(lecturer, course.getLecturer());
        assertEquals(semester, course.getSemester());
        assertEquals(duration, course.getDuration());
        assertEquals(numberOfParticipants, course.getNumberOfParticipants());
        assertEquals(computersNecessary, course.isComputersNecessary());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDeleteCourseWithCourseSession() {
        Course course = courseService.loadCourseById("703003");

        courseService.deleteCourse(course);

        assertThrows(EntityNotFoundException.class, () -> courseService.loadCourseById("703003"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDeleteMultipleCourses() {
        Course course1 = courseService.loadCourseById("703003");
        Course course2 = courseService.loadCourseById("703004");

        courseService.deleteMultipleCourses(List.of(course1, course2));

        assertThrows(EntityNotFoundException.class, () -> courseService.loadCourseById("703003"));
        assertThrows(EntityNotFoundException.class, () -> courseService.loadCourseById("703004"));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUpdateCourse() {
        Course course = courseService.loadCourseById("703063");
        String newLecturer = "Peter Gruber";
        courseService.updateCourse(course, course.getName(), course.getCourseType(), newLecturer,
                course.getSemester(), course.getDuration(), course.getNumberOfParticipants(), course.isComputersNecessary(),
                course.getTimingConstraints());

        assertEquals("Peter Gruber", course.getLecturer());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadCourseById() {
        Course course = courseService.loadCourseById("703010");

        assertEquals(2, course.getTimingConstraints().size());
        assertEquals(CourseType.VO, course.getCourseType());
        assertEquals("Algorithmen und Datenstrukturen", course.getName());
        assertEquals("Justus Piater", course.getLecturer());
        assertEquals(180, course.getDuration());
        assertEquals(300, course.getNumberOfParticipants());
        assertEquals(2, course.getSemester());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadAllCourses() {
        List<Course> courses = courseService.loadAllCourses();
        assertEquals(18, courses.size());
    }
}