package com.lecture.coordinator.services;

import com.lecture.coordinator.model.*;
import com.lecture.coordinator.repositories.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Scope("session")
public class TimeTableService {
    @Autowired
    private TimeTableRepository timeTableRepository;
    @Autowired
    private RoomTableService roomTableService;
    @Autowired
    private CourseSessionService courseSessionService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TimeTable createTimeTable(Semester semester, int year){
        TimeTable timeTable = new TimeTable();
        timeTable.setSemester(semester);
        timeTable.setYear(year);

        return timeTableRepository.save(timeTable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public RoomTable addRoomTable(TimeTable timeTable, Room room){
        RoomTable roomTable = roomTableService.createRoomTableFromRoom(timeTable, room);
        timeTable.addRoomTable(roomTable);

        return roomTable;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<CourseSession> addCourseSessions(TimeTable timeTable, Course course){
        List<CourseSession> courseSessions = courseSessionService.createCourseSessionsFromCourse(course);
        timeTable.addCourseSessions(courseSessions);

        return courseSessions;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void removeRoomTable(TimeTable timeTable, RoomTable roomTable){
        //TODO: implement
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void removeCourseSession(TimeTable timeTable, CourseSession courseSession){
        //TODO: implement
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<TimeTable> loadAllTimeTables(){
        return timeTableRepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TimeTable loadTimeTable(long id){
        TimeTable timeTable = timeTableRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("TimeTable not found for ID: " + id));
        List<RoomTable> roomTables = roomTableService.loadAllOfTimeTable(timeTable);
        List<CourseSession> courseSessions = courseSessionService.loadAllFromTimeTable(timeTable);
        timeTable.setRoomTables(roomTables);
        timeTable.setCourseSessions(courseSessions);
        return timeTable;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void assignCourseSessionsToRooms(TimeTable timeTable){
        //TODO: This is the place where our ALGORITHM will be executed
    }
}
