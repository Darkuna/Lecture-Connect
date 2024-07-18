package com.example.demo.services;

import com.example.demo.dto.*;
import com.example.demo.models.*;
import com.example.demo.models.enums.Semester;
import com.example.demo.models.enums.Status;
import com.example.demo.repositories.TimeTableRepository;
import com.example.demo.scheduling.Scheduler;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

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
    @Autowired
    private TimingService timingService;
    @Autowired
    private DTOConverter dtoConverter;

    /**
     * Creates a new timetable from a TimeTableCreationDTO object and saves it to the database.
     *
     * @param dto The TimeTableCreationDTO object to create the timetable from.
     * @return The newly created and persisted TimeTable object.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TimeTable createTimeTable(TimeTableCreationDTO dto){
        Room room;
        Course course;
        TimeTable timeTable = new TimeTable();
        timeTable.setStatus(Status.valueOf(dto.getStatus()));
        timeTable.setSemester(Semester.valueOf(dto.getSemester()));
        timeTable.setYear(dto.getYear());

        timeTable = timeTableRepository.save(timeTable);

        for(RoomDTO roomDTO : dto.getRooms()){
            room = dtoConverter.toRoom(roomDTO);
            addRoomTable(timeTable, room);
        }
        for(CourseDTO courseDTO : dto.getCourses()){
            course = dtoConverter.toCourse(courseDTO);
            addCourseSessions(timeTable, course);
        }
        return timeTable;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TimeTable createTimeTable(Semester semester, int year){
        TimeTable timeTable = new TimeTable();
        timeTable.setSemester(semester);
        timeTable.setYear(year);
        timeTable.setStatus(Status.NEW);
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
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void removeRoomTable(TimeTable timeTable, RoomTable roomTable){
        timeTable.removeRoomTable(roomTable);
        roomTableService.deleteRoomTable(roomTable);
    }

    /**
     * Removes a specific course session from a timetable.
     *
     * @param timeTable The timetable from which the course session will be removed.
     * @param courseSession The course session to be removed.
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void removeCourseSession(TimeTable timeTable, CourseSession courseSession){
        timeTable.removeCourseSession(courseSession);
        courseSessionService.deleteCourseSession(courseSession);
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
        timeTable.setScheduler(new Scheduler(timeTable));
        return timeTable;
    }

    /**
     * Deletes a timetable, also deleting all associated course sessions and room tables.
     *
     * @param timeTable Timetable to be deleted.
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteTimeTable(TimeTable timeTable){
        for(CourseSession courseSession : List.copyOf(timeTable.getCourseSessions())){
            removeCourseSession(timeTable, courseSession);
        }

        for(RoomTable roomTable : List.copyOf(timeTable.getRoomTables())){
            removeRoomTable(timeTable, roomTable);
        }
        //TODO: fix why is there a new courseSession with ID 1 associated with timetable
        courseSessionService.deleteCourseSession(courseSessionService.loadCourseSessionByID(1));

        timeTableRepository.delete(timeTable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<TimeTableNameDTO> loadTimeTableNames(){
        return timeTableRepository.findAllTimeTableDTOs();
    }

    /**
     * Assigns all course sessions that are not assigned yet to room tables within a timetable.
     * This is the place where the algorithm for scheduling course sessions into room tables will be executed.
     *
     * @param timeTable The timetable for executing the algorithm
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void assignCourseSessionsToRooms(TimeTable timeTable){
        if(timeTable.getScheduler() == null){
            timeTable.setScheduler(new Scheduler(timeTable));
        }
        timeTable.getScheduler().assignUnassignedCourseSessions();
        for(CourseSession courseSession : timeTable.getCourseSessions()){
            courseSession.setTiming(timingService.createTiming(courseSession.getTiming().getStartTime(), courseSession.getTiming().getEndTime(), courseSession.getTiming().getDay()));
        }
        timeTable.setCourseSessions(courseSessionService.saveAll(timeTable.getCourseSessions()));
        timeTableRepository.save(timeTable);
    }
}
