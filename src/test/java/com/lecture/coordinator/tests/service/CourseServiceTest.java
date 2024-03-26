package com.lecture.coordinator.tests.service;

import com.lecture.coordinator.model.*;
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
        String lecturer = "Johannes Karrer";
        int duration = 2;
        int semester = 1;
        int numberOfParticipants = 35;
        int numberOfGroups = 6;
        boolean isSplit = false;
        List<Integer> splitTimes = null;
        boolean computersNecessary = false;
        boolean isTimingFixed = false;
        TimingTuple fixedTimings = null;
        Timing constraint = timingService.createTiming(LocalTime.of(12,0), LocalTime.of(14,30),Day.MONDAY);
        List<Timing> timingConstraints = List.of(constraint);

        Course course = courseService.createCourse(id, name, lecturer, semester, duration, numberOfParticipants, numberOfGroups,
                isSplit, splitTimes, computersNecessary, isTimingFixed, fixedTimings, timingConstraints);

        assertEquals(id, course.getId());
        assertEquals(name, course.getName());
        assertEquals(lecturer, course.getLecturer());
        assertEquals(semester, course.getSemester());
        assertEquals(duration, course.getDuration());
        assertEquals(numberOfParticipants, course.getNumberOfParticipants());
        assertEquals(numberOfGroups, course.getNumberOfGroups());
        assertEquals(isSplit, course.isSplit());
        assertEquals(splitTimes, course.getSplitTimes());
        assertEquals(computersNecessary, course.isComputersNecessary());
        assertEquals(isTimingFixed, course.isTimingFixed());
        assertEquals(fixedTimings, course.getFixedTimings());
    }
}