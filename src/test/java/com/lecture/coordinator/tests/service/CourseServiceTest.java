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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("dev")
public class CourseServiceTest {
    @Autowired
    private CourseService courseService;
    @Autowired
    private TimingService timingService;

    @Test
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
}