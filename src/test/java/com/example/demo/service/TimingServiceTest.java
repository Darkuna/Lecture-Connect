package com.example.demo.service;

import at.uibk.leco.constants.TimingConstants;
import at.uibk.leco.models.RoomTable;
import at.uibk.leco.models.Timing;
import at.uibk.leco.models.enums.Day;
import at.uibk.leco.models.enums.TimingType;
import at.uibk.leco.services.CourseService;
import at.uibk.leco.services.RoomTableService;
import at.uibk.leco.services.TimingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import jakarta.persistence.EntityNotFoundException;
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
    private RoomTableService roomTableService;
    @Autowired
    private CourseService courseService;

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateTimingWithCorrectParameters(){
        Day day = Day.MONDAY;
        LocalTime start = LocalTime.of(12,0);
        LocalTime end = LocalTime.of(14,0);
        Timing timing = timingService.createTiming(start, end, day, TimingType.ASSIGNED);

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

        assertThrows(IllegalArgumentException.class, () -> timingService.createTiming(start, end, day,
                TimingType.ASSIGNED));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUpdateTimingWithCorrectParameters(){
        Timing timing = timingService.loadTimingByID(-2);
        Day day = Day.FRIDAY;
        LocalTime start = TimingConstants.START_TIME;
        LocalTime end = LocalTime.of(10,0);
        timing = timingService.updateTiming(timing, start, end, day, TimingType.ASSIGNED);

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

        assertThrows(IllegalArgumentException.class, () -> timingService.updateTiming(timing, start, end, day,
                TimingType.ASSIGNED));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimingConstraintsOfRoomTable(){
        int numberOfConstraints = 7;
        RoomTable roomTable = roomTableService.loadRoomTableByID(-90);
        List<Timing> timingConstraints = timingService.loadTimingConstraintsOfRoomTable(roomTable);

        assertEquals(numberOfConstraints, timingConstraints.size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimingConstraintsOfCourse(){
        String courseId = "703010";
        int numberOfConstraints = 2;
        List<Timing> timingConstraints = timingService.loadTimingConstraintsOfCourse(courseId);

        assertEquals(numberOfConstraints, timingConstraints.size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimingWithWrongID(){
        assertThrows(EntityNotFoundException.class, () -> timingService.loadTimingByID(-1000));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimingConstraintsOfCourseByCourseId(){
        System.out.println(timingService.loadTimingConstraintsOfCourse("703010"));
    }
}
