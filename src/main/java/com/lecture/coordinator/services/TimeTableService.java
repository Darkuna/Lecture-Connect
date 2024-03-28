package com.lecture.coordinator.services;

import com.lecture.coordinator.model.*;
import com.lecture.coordinator.repositories.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
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
    public TimeTable createTimeTable(Semester semester, int year, List<Room> rooms, List<Course> courses){
        TimeTable timeTable = new TimeTable();
        timeTable.setSemester(semester);
        timeTable.setYear(year);
        timeTable.setRooms(rooms);
        timeTable.setCourses(courses);

        timeTableRepository.save(timeTable);

        createRoomTables(timeTable);
        createCourseSessions(timeTable);

        return timeTable;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    private void createRoomTables(TimeTable timeTable){
        List<RoomTable> roomTables = new ArrayList<>();
        for(Room room : timeTable.getRooms()){
            RoomTable roomTable = roomTableService.createRoomTableFromRoom(timeTable, room);
            roomTable.setTimeTable(timeTable);
            roomTables.add(roomTable);
        }
        timeTable.setRoomTables(roomTables);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    private void createCourseSessions(TimeTable timeTable){
        List<CourseSession> combinedCourseSessions = new ArrayList<>();
        List<CourseSession> courseSessions;

        for(Course course : timeTable.getCourses()){
            courseSessions = courseSessionService.createCourseSessionsFromCourse(course);
            for(CourseSession courseSession : courseSessions){
                courseSession.setTimeTable(timeTable);
            }
            combinedCourseSessions.addAll(courseSessions);
        }
        timeTable.setCourseSessions(combinedCourseSessions);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void addRoom(TimeTable timeTable, Room room){
        timeTable.addRoom(room);
        timeTable.addRoomTable(roomTableService.createRoomTableFromRoom(timeTable, room));
        timeTableRepository.save(timeTable);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void addCourse(TimeTable timeTable, Course course){
        List<Course> courses = timeTable.getCourses();
        courses.add(course);
        List<CourseSession> courseSessions = timeTable.getCourseSessions();
        List<CourseSession> courseSessionsToAdd = courseSessionService.createCourseSessionsFromCourse(course);
        courseSessions.addAll(courseSessionsToAdd);
        timeTableRepository.save(timeTable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void removeRoom(TimeTable timeTable, Room room){
        List<Room> rooms = timeTable.getRooms();
        rooms.remove(room);
        List<RoomTable> roomTables = timeTable.getRoomTables();
        roomTables.remove(roomTableService.loadRoomTableByRoom(room));
        timeTableRepository.save(timeTable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void removeCourse(TimeTable timeTable, Course course){
        List<CourseSession> courseSessions = timeTable.getCourseSessions();
        courseSessions.removeAll(courseSessionService.loadAllFromTimeTableAndCourse(timeTable,course));
        List<Course> courses = timeTable.getCourses();
        courses.remove(course);
        timeTableRepository.save(timeTable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<TimeTable> loadAllTimeTables(){
        return timeTableRepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TimeTable loadTimeTable(long id){
        TimeTable timeTable = timeTableRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("TimeTable not found for ID: " + id));
        List<RoomTable> roomTables = roomTableService.loadRoomTablesOfTimeTable(timeTable);
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
