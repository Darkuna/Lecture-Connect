package com.lecture.coordinator.tests.service;

import com.lecture.coordinator.model.*;
import com.lecture.coordinator.services.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@WebAppConfiguration
public class CourseServiceTest {
    @Autowired
    private CourseService courseService;

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseWithoutSplitOrGroupsOrFixedTiming(){
        String id = "lv232323";
        String name = "TestCourse";
        String lecturer = "Johannes Karrer";
        int duration = 2;
        int numberOfParticipants = 35;
        int numberOfGroups = 1;
        boolean isSplit = false;
        IntegerTuple splitTimes = null;
        boolean computersNecessary = false;
        boolean isTimingFixed = false;
        TimingTuple fixedTimings = null;
        Timing constraint = new Timing();
        constraint.setDay(Day.MONDAY);
        constraint.setStartTime(LocalTime.of(12,0));
        constraint.setEndTime(LocalTime.of(14,30));
        List<Timing> timingConstraints = List.of(constraint);

        Course course = courseService.createCourse(id, name, lecturer, duration, numberOfParticipants, numberOfGroups,
                isSplit, splitTimes, computersNecessary, isTimingFixed, fixedTimings, timingConstraints);

        assertEquals(id, course.getId());
        assertEquals(name, course.getName());
        assertEquals(lecturer, course.getLecturer());
        assertEquals(duration, course.getDuration());
        assertEquals(numberOfParticipants, course.getNumberOfParticipants());

        assertEquals(1, course.getCourseSessions().size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseWithSplitAndWithoutGroupsOrFixedTiming(){
        String id = "lv232324";
        String name = "TestCourse";
        String lecturer = "Johannes Karrer";
        int duration = 2;
        int numberOfParticipants = 35;
        int numberOfGroups = 1;
        boolean isSplit = true;
        IntegerTuple splitTimes = new IntegerTuple();
        splitTimes.setL(2);
        splitTimes.setR(1);
        boolean computersNecessary = false;
        boolean isTimingFixed = false;
        TimingTuple fixedTimings = null;
        Timing constraint = new Timing();
        constraint.setDay(Day.MONDAY);
        constraint.setStartTime(LocalTime.of(12,0));
        constraint.setEndTime(LocalTime.of(14,30));
        List<Timing> timingConstraints = List.of(constraint);

        Course course = courseService.createCourse(id, name, lecturer, duration, numberOfParticipants, numberOfGroups,
                isSplit, splitTimes, computersNecessary, isTimingFixed, fixedTimings, timingConstraints);

        assertEquals(id, course.getId());
        assertEquals(name, course.getName());
        assertEquals(lecturer, course.getLecturer());
        assertEquals(duration, course.getDuration());
        assertEquals(numberOfParticipants, course.getNumberOfParticipants());

        assertEquals(2, course.getCourseSessions().size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseWithGroupsAndWithoutSplitOrFixedTiming(){
        String id = "lv232325";
        String name = "TestCourse";
        String lecturer = "Johannes Karrer";
        int duration = 2;
        int numberOfParticipants = 35;
        int numberOfGroups = 6;
        boolean isSplit = false;
        IntegerTuple splitTimes = null;
        boolean computersNecessary = false;
        boolean isTimingFixed = false;
        TimingTuple fixedTimings = null;
        Timing constraint = new Timing();
        constraint.setDay(Day.MONDAY);
        constraint.setStartTime(LocalTime.of(12,0));
        constraint.setEndTime(LocalTime.of(14,30));
        List<Timing> timingConstraints = List.of(constraint);

        Course course = courseService.createCourse(id, name, lecturer, duration, numberOfParticipants, numberOfGroups,
                isSplit, splitTimes, computersNecessary, isTimingFixed, fixedTimings, timingConstraints);

        assertEquals(id, course.getId());
        assertEquals(name, course.getName());
        assertEquals(lecturer, course.getLecturer());
        assertEquals(duration, course.getDuration());
        assertEquals(numberOfParticipants, course.getNumberOfParticipants());

        List<CourseSession> courseSessions = course.getCourseSessions();
        assertEquals(6, courseSessions.size());
        for(int i = 0; i < 6; i++){
            assertEquals(String.format("%s-%d",course.getId(),i), courseSessions.get(i).getId());
            assertEquals(duration, courseSessions.get(i).getDuration());
            assertEquals(timingConstraints, courseSessions.get(i).getTimingConstraints());
            assertEquals(false, courseSessions.get(i).isAssigned());
        }
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseWithFixedTimingAndWithoutSplitOrGroups(){
        String id = "lv232326";
        String name = "TestCourse";
        String lecturer = "Johannes Karrer";
        int duration = 1;
        int numberOfParticipants = 35;
        int numberOfGroups = 1;
        boolean isSplit = false;
        IntegerTuple splitTimes = null;
        boolean computersNecessary = false;
        boolean isTimingFixed = true;
        TimingTuple fixedTimings = new TimingTuple();
        Timing timing = new Timing();
        timing.setDay(Day.FRIDAY);
        timing.setStartTime(LocalTime.of(12,0));
        timing.setEndTime(LocalTime.of(13,0));
        fixedTimings.setL(timing);
        List<Timing> timingConstraints = null;

        Course course = courseService.createCourse(id, name, lecturer, duration, numberOfParticipants, numberOfGroups,
                isSplit, splitTimes, computersNecessary, isTimingFixed, fixedTimings, timingConstraints);

        assertEquals(id, course.getId());
        assertEquals(name, course.getName());
        assertEquals(lecturer, course.getLecturer());
        assertEquals(duration, course.getDuration());
        assertEquals(numberOfParticipants, course.getNumberOfParticipants());

        List<CourseSession> courseSessions = course.getCourseSessions();
        assertEquals(1, courseSessions.size());
        assertEquals(String.format("%s-%d",course.getId(),0), courseSessions.get(0).getId());
        assertEquals(duration, courseSessions.get(0).getDuration());
        assertEquals(null, courseSessions.get(0).getTimingConstraints());
        assertEquals(true, courseSessions.get(0).isAssigned());

        Timing sessionTiming = courseSessions.get(0).getTiming();
        assertEquals(Day.FRIDAY, sessionTiming.getDay());
        assertEquals(LocalTime.of(12,0), sessionTiming.getStartTime());
        assertEquals(LocalTime.of(13,0), sessionTiming.getEndTime());
    }
}
