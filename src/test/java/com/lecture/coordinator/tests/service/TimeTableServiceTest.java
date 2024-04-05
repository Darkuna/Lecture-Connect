package com.lecture.coordinator.tests.service;

import com.lecture.coordinator.model.*;
import com.lecture.coordinator.model.enums.Semester;
import com.lecture.coordinator.services.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("dev")
public class TimeTableServiceTest {
    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private RoomTableService roomTableService;
    @Autowired
    private CourseSessionService courseSessionService;

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateTimeTableWithOneRoomAndOneCourseWithoutGroups(){
        TimeTable timeTable = timeTableService.createTimeTable(Semester.SS, 2024);

        assertEquals(Semester.SS, timeTable.getSemester());
        assertEquals(2024, timeTable.getYear());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testAddRoomTableToTimeTable(){
        TimeTable timeTable = timeTableService.createTimeTable(Semester.SS, 2024);
        Room room = roomService.loadRoomByID("HS A");
        RoomTable roomTable = timeTableService.addRoomTable(timeTable, room);

        assertEquals(room, roomTable.getRoom());
        assertEquals(timeTable, roomTable.getTimeTable());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testAddCourseSessionToTimeTable(){
        TimeTable timeTable = timeTableService.createTimeTable(Semester.SS, 2024);
        Course course = courseService.loadCourseById("703003");
        course.setNumberOfGroups(6);
        course.setSplit(false);
        course.setSplitTimes(null);
        List<CourseSession> courseSessions = timeTableService.addCourseSessions(timeTable, course);

        assertEquals(course.getNumberOfGroups(), courseSessions.size());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testRemoveRoomTableFromTimeTable(){
        TimeTable timeTable = timeTableService.loadTimeTable(-1);
        int numberOfRoomTables = timeTable.getRoomTables().size();
        RoomTable roomTable = timeTable.getRoomTables().get(0);
        Long roomTableID = roomTable.getId();

        timeTableService.removeRoomTable(timeTable, roomTable);

        assertEquals(numberOfRoomTables-1, timeTable.getRoomTables().size());
        assertThrows(EntityNotFoundException.class, () -> roomTableService.loadRoomTableByID(roomTableID));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testRemoveUnassignedCourseSessionFromTimeTable(){
        TimeTable timeTable = timeTableService.loadTimeTable(-1);
        int numberOfCourseSessions = timeTable.getCourseSessions().size();
        CourseSession courseSession = timeTable.getCourseSessions().get(0);
        Long courseSessionId = courseSession.getId();

        timeTableService.removeCourseSession(timeTable, courseSession);

        assertEquals(numberOfCourseSessions-1, timeTable.getCourseSessions().size());
        assertThrows(EntityNotFoundException.class, () -> courseSessionService.loadCourseSessionByID(courseSessionId));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testRemoveAssignedCourseSessionFromTimeTable(){
        TimeTable timeTable = timeTableService.loadTimeTable(-1);
        CourseSession courseSession = courseSessionService.loadCourseSessionByID(-6);
        int numberOfCourseSessions = timeTable.getCourseSessions().size();

        timeTableService.removeCourseSession(timeTable, courseSession);

        assertEquals(numberOfCourseSessions-1, timeTable.getCourseSessions().size());
        assertThrows(EntityNotFoundException.class, () -> courseSessionService.loadCourseSessionByID(-6));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDeleteTimeTable(){
        TimeTable timeTable = timeTableService.loadTimeTable(-1);
        List<Long> roomTableIDs = timeTable.getRoomTables().stream().map(RoomTable::getId).toList();
        List<Long> courseSessionIDs = timeTable.getCourseSessions().stream().map(CourseSession::getId).toList();

        timeTableService.deleteTimeTable(timeTable);

        assertThrows(EntityNotFoundException.class, () -> timeTableService.loadTimeTable(-1));

        for(Long roomTableId : roomTableIDs){
            assertThrows(EntityNotFoundException.class, () -> roomTableService.loadRoomTableByID(roomTableId));
        }
        for(Long courseSessionId : courseSessionIDs){
            assertThrows(EntityNotFoundException.class, () -> courseSessionService.loadCourseSessionByID(courseSessionId));
        }
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimeTables(){
        List<TimeTable> timeTables = timeTableService.loadAllTimeTables();

        assertEquals(1, timeTables.size());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimeTable(){
        TimeTable timeTable = timeTableService.loadTimeTable(-1);

        assertEquals(Semester.SS, timeTable.getSemester());
        assertEquals(2023, timeTable.getYear());
        assertNotNull(timeTable.getRoomTables());
        assertNotNull(timeTable.getCourseSessions());
    }

}
