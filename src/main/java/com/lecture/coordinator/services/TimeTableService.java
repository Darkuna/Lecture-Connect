package com.lecture.coordinator.services;

import com.lecture.coordinator.model.*;
import com.lecture.coordinator.model.enums.Semester;
import com.lecture.coordinator.repositories.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Service class for managing timetables.
 * This service provides functionalities to create and manipulate timetables,
 * including adding or removing room tables and course sessions.
 */
@Service
@Scope("session")
public class TimeTableService {
    @Autowired
    private TimeTableRepository timeTableRepository;
    @Autowired
    private RoomTableService roomTableService;
    @Autowired
    private CourseSessionService courseSessionService;

    /**
     * Creates a new timetable for a specific semester and year, and saves it to the database.
     *
     * @param semester The semester for which the timetable is being created.
     * @param year The year for which the timetable is being created.
     * @return The newly created and persisted TimeTable object.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TimeTable createTimeTable(Semester semester, int year){
        TimeTable timeTable = new TimeTable();
        timeTable.setSemester(semester);
        timeTable.setYear(year);

        return timeTableRepository.save(timeTable);
    }

    /**
     * Adds a room table to a specific timetable based on the provided room.
     * The room table is created and then associated with the timetable.
     *
     * @param timeTable The timetable to which the room table will be added.
     * @param room The room based on which the room table will be created.
     * @return The newly created RoomTable object.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public RoomTable addRoomTable(TimeTable timeTable, Room room){
        RoomTable roomTable = roomTableService.createRoomTableFromRoom(timeTable, room);
        timeTable.addRoomTable(roomTable);

        return roomTable;
    }

    /**
     * Adds course sessions to a specific timetable based on the provided course.
     * The course sessions are created and then associated with the timetable.
     *
     * @param timeTable The timetable to which the course sessions will be added.
     * @param course The course based on which the course sessions will be created.
     * @return A list of the newly created CourseSession objects.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<CourseSession> addCourseSessions(TimeTable timeTable, Course course){
        List<CourseSession> courseSessions = courseSessionService.createCourseSessionsFromCourse(course);
        timeTable.addCourseSessions(courseSessions);

        return courseSessions;
    }

    /**
     * Removes a specific room table from a timetable.
     *
     * @param timeTable The timetable from which the room table will be removed.
     * @param roomTable The room table to be removed.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void removeRoomTable(TimeTable timeTable, RoomTable roomTable){
        //TODO: delete the roomTable, unassigning all associated course sessions
    }

    /**
     * Removes a specific course session from a timetable.
     *
     * @param timeTable The timetable from which the course session will be removed.
     * @param courseSession The course session to be removed.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void removeCourseSession(TimeTable timeTable, CourseSession courseSession){
        //TODO: implement
    }

    /**
     * Loads all timetables available in the database.
     *
     * @return A list of all TimeTable objects.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<TimeTable> loadAllTimeTables(){
        return timeTableRepository.findAll();
    }

    /**
     * Loads a specific timetable by its ID, along with its associated room tables and course sessions.
     *
     * @param id The ID of the timetable to be loaded.
     * @return The TimeTable object associated with the given ID.
     * @throws EntityNotFoundException If no timetable is found for the provided ID.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TimeTable loadTimeTable(long id){
        TimeTable timeTable = timeTableRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("TimeTable not found for ID: " + id));
        List<RoomTable> roomTables = roomTableService.loadAllOfTimeTable(timeTable);
        List<CourseSession> courseSessions = courseSessionService.loadAllFromTimeTable(timeTable);
        timeTable.setRoomTables(roomTables);
        timeTable.setCourseSessions(courseSessions);
        return timeTable;
    }

    /**
     * Assigns all course sessions that are not assigned yet to room tables within a timetable.
     * This is the place where the algorithm for scheduling course sessions into room tables will be executed.
     *
     * @param timeTable The timetable for executing the algorithm
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void assignCourseSessionsToRooms(TimeTable timeTable){
        //TODO: This is the place where our ALGORITHM will be executed
    }
}
