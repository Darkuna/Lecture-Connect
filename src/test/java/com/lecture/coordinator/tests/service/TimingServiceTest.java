package com.lecture.coordinator.tests.service;

import com.lecture.coordinator.model.Course;
import com.lecture.coordinator.model.Day;
import com.lecture.coordinator.model.Room;
import com.lecture.coordinator.model.Timing;
import com.lecture.coordinator.services.CourseService;
import com.lecture.coordinator.services.RoomService;
import com.lecture.coordinator.services.TimingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
@WebAppConfiguration
public class TimingServiceTest {
    @Autowired
    private TimingService timingService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private CourseService courseService;

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateTimingWithCorrectParameters(){
        Day day = Day.MONDAY;
        LocalTime start = LocalTime.of(12,0);
        LocalTime end = LocalTime.of(14,0);
        Timing timing = timingService.createTiming(start, end, day);

        assertEquals(day, timing.getDay());
        assertEquals(start, timing.getStartTime());
        assertEquals(end, timing.getEndTime());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateTimingWithWrongParameters(){
        Day day = Day.MONDAY;
        LocalTime start = LocalTime.of(14,0);
        LocalTime end = LocalTime.of(12,0);

        assertThrows(IllegalArgumentException.class, () -> timingService.createTiming(start, end, day));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUpdateTimingWithCorrectParameters(){
        Timing timing = timingService.loadTimingByID(-2);
        Day day = Day.FRIDAY;
        LocalTime start = LocalTime.of(8,0);
        LocalTime end = LocalTime.of(10,0);
        timing = timingService.updateTiming(timing, start, end, day);

        assertEquals(day, timing.getDay());
        assertEquals(start, timing.getStartTime());
        assertEquals(end, timing.getEndTime());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUpdateTimingWithWrongParameters(){
        Timing timing = timingService.loadTimingByID(-1);
        Day day = Day.FRIDAY;
        LocalTime start = LocalTime.of(10,0);
        LocalTime end = LocalTime.of(8,0);

        assertThrows(IllegalArgumentException.class, () -> timingService.updateTiming(timing, start, end, day));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimingConstraintsOfRoom(){
        int numberOfConstraints = 6;
        Room room = roomService.loadRoomByID("HS A");
        List<Timing> timingConstraints = timingService.loadTimingConstraintsOfRoom(room);

        assertEquals(numberOfConstraints, timingConstraints.size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimingConstraintsOfCourse(){
        int numberOfConstraints = 2;
        Course course = courseService.loadCourseById("703010");
        List<Timing> timingConstraints = timingService.loadTimingConstraintsOfCourse(course);

        assertEquals(numberOfConstraints, timingConstraints.size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimingWithWrongID(){
        assertThrows(EntityNotFoundException.class, () -> timingService.loadTimingByID(-1000));
    }
}
