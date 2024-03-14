package com.lecture.coordinator.services;

import com.lecture.coordinator.model.*;
import com.lecture.coordinator.repositories.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

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

    public TimeTable createTimeTable(Semester semester, int year, List<Room> rooms, List<Course> courses){
        TimeTable timeTable = new TimeTable();
        timeTable.setSemester(semester);
        timeTable.setYear(year);
        timeTable.setRooms(rooms);
        timeTable.setCourses(courses);

        return timeTableRepository.save(timeTable);
    }

    public void createRoomTables(TimeTable timeTable){
        List<RoomTable> roomTables = new ArrayList<>();
        for(Room room : timeTable.getRooms()){
            RoomTable roomTable = roomTableService.createRoomTableFromRoom(room);
            roomTable.setTimeTable(timeTable);
            roomTables.add(roomTable);
        }
        timeTable.setRoomTables(roomTables);
    }

    public void createCourseSessions(TimeTable timeTable){
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

    public void addRoom(TimeTable timeTable, Room room){
        //TODO: Add room to timeTable.rooms and create RoomTable
    }

    public void addCourse(TimeTable timeTable, Course course){
        //TODO: Add course to timeTable.courses and add the added course's course sessions to
        // timeTable.courseSessions
    }

    public void assignCourseSessionsToRooms(TimeTable timeTable){
        //TODO: This is the place where our ALGORITHM will be executed
    }
}
