package com.example.demo.services;

import com.example.demo.dto.TimeTableDTO;
import com.example.demo.dto.TimeTableNameDTO;
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
import java.util.stream.Collectors;

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

    /**
     * Creates a new timetable for a specific semester and year, and saves it to the database.
     *
     * @param semester The semester for which the timetable is being created.
     * @param year The year for which the timetable is being created.
     * @return The newly created and persisted TimeTable object.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TimeTable createTimeTable(Semester semester, int year, Status status){
        TimeTable timeTable = new TimeTable();
        timeTable.setStatus(status);
        timeTable.setSemester(semester);
        timeTable.setYear(year);

        return timeTableRepository.save(timeTable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TimeTable createTimeTable(Semester semester, int year, Status status, List<Course> courses, List<Room> rooms){
        TimeTable timeTable = new TimeTable();
        timeTable.setStatus(status);
        timeTable.setSemester(semester);
        timeTable.setYear(year);
        for(Room room : rooms){
            addRoomTable(timeTable, room);
        }
        for(Course course : courses){
            addCourseSessions(timeTable, course);
        }
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
     * Converts a TimeTable object into a TimeTableDTO object
     *
     * @param timeTable to be converted
     * @return TimeTableDTO object
     */
    public TimeTableDTO toDTO(TimeTable timeTable){
        TimeTableDTO dto = new TimeTableDTO();
        dto.setId(timeTable.getId());
        dto.setSemester(timeTable.getSemester().toString());
        dto.setYear(timeTable.getYear());
        dto.setStatus(timeTable.getStatus().toString());
        dto.setCreatedAt(timeTable.getCreatedAt());
        dto.setUpdatedAt(timeTable.getUpdatedAt());

        dto.setRoomTables(timeTable.getRoomTables().stream()
                .map(roomTableService::toDTO)
                .collect(Collectors.toList()));

        dto.setCourseSessions(timeTable.getCourseSessions().stream()
                .map(courseSessionService::toDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    /**
     * Converts a TimeTableDTO object into a TimeTable object
     *
     * @param dto to be converted
     * @return TimeTable object
     */
    public TimeTable fromDTO(TimeTableDTO dto) {
        TimeTable timeTable = new TimeTable();
        timeTable.setId(dto.getId());
        timeTable.setSemester(Semester.valueOf(dto.getSemester()));
        timeTable.setStatus(Status.valueOf(dto.getStatus()));
        timeTable.setYear(dto.getYear());
        timeTable.setCreatedAt(dto.getCreatedAt());
        timeTable.setUpdatedAt(dto.getUpdatedAt());

        timeTable.setRoomTables(dto.getRoomTables().stream()
                .map(roomTableService::fromDTO)
                .collect(Collectors.toList()));

        timeTable.setCourseSessions(dto.getCourseSessions().stream()
                .map(courseSessionService::fromDTO)
                .collect(Collectors.toList()));

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
