package com.example.demo.services;

import com.example.demo.dto.*;
import com.example.demo.exceptions.scheduler.AssignmentFailedException;
import com.example.demo.models.*;
import com.example.demo.models.enums.ChangeType;
import com.example.demo.models.enums.Semester;
import com.example.demo.models.enums.Status;
import com.example.demo.repositories.TimeTableRepository;
import com.example.demo.scheduling.CollisionType;
import com.example.demo.scheduling.Scheduler;
import com.example.demo.scheduling.BacktrackingScheduler;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.Map;

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
    private final TimeTableRepository timeTableRepository;
    private final RoomTableService roomTableService;
    private final CourseSessionService courseSessionService;
    private final DTOConverter dtoConverter;
    private final Scheduler scheduler;
    private final GlobalTableChangeService globalTableChangeService;
    private static final Logger log = LoggerFactory.getLogger(TimeTableService.class);

    public TimeTableService(TimeTableRepository timeTableRepository, RoomTableService roomTableService,
                            CourseSessionService courseSessionService, DTOConverter dtoConverter, BacktrackingScheduler scheduler,
                            GlobalTableChangeService globalTableChangeService) {
        this.timeTableRepository = timeTableRepository;
        this.roomTableService = roomTableService;
        this.courseSessionService = courseSessionService;
        this.dtoConverter = dtoConverter;
        this.scheduler = scheduler;
        this.globalTableChangeService = globalTableChangeService;
    }

    /**
     * Creates a new timetable from a TimeTableCreationDto object and saves it to the database.
     * @Transactional is important here because in case of an error the timeTable would be partially created otherwise.
     * @param dto The TimeTableCreationDto object to create the timetable from.
     * @return The newly created and persisted TimeTable object.
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TimeTable createTimeTable(TimeTableCreationDTO dto){
        Room room;
        Course course;
        TimeTable timeTable = new TimeTable();
        timeTable.setName(dto.getName());
        timeTable.setStatus(Status.NEW);
        timeTable.setSemester(Semester.valueOf(dto.getSemester()));
        timeTable.setYear(dto.getYear());

        timeTable = timeTableRepository.save(timeTable);

        for(RoomDTO roomDTO : dto.getRooms()){
            room = dtoConverter.toRoom(roomDTO);
            createRoomTable(timeTable, room);
        }
        for(CourseDTO courseDTO : dto.getCourses()){
            course = dtoConverter.toCourse(courseDTO);
            createCourseSessions(timeTable, course);
        }
        log.info("Created timeTable with id {}. Added {} roomTables and {} courseSessions", timeTable.getId(),
                timeTable.getRoomTables().size(), timeTable.getCourseSessions().size());
        globalTableChangeService.create(ChangeType.CREATE_TABLE, timeTable, String.format("Timetable '%s %d' with id '%d' was created",
                dto.getSemester(), dto.getYear(), timeTable.getId()));
        return timeTableRepository.save(timeTable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TimeTable createTimeTable(String name, Semester semester, int year){
        TimeTable timeTable = new TimeTable();
        timeTable.setName(name);
        timeTable.setSemester(semester);
        timeTable.setYear(year);
        timeTable.setStatus(Status.NEW);
        timeTable = timeTableRepository.save(timeTable);
        log.info("Created timeTable with id {}", timeTable.getId());
        return timeTable;
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
    public RoomTable createRoomTable(TimeTable timeTable, Room room){
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
    public List<CourseSession> createCourseSessions(TimeTable timeTable, Course course){
        List<CourseSession> courseSessions = courseSessionService.createCourseSessionsFromCourse(timeTable, course);
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
        log.info("Removed roomTable with id {} from timeTable {}", roomTable.getId(), timeTable.getId());
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
        log.info("Removed courseSession with id {} from timeTable {}", courseSession.getId(), timeTable.getId());
        courseSessionService.deleteCourseSession(courseSession);
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
        scheduler.setTimeTable(timeTable);
        log.info("Loaded timeTable with id {} ", id);
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
        unassignAllCourseSessions(timeTable);
        for(CourseSession courseSession : timeTable.getCourseSessions()){
            removeCourseSession(timeTable, courseSession);
        }

        for(RoomTable roomTable : timeTable.getRoomTables()){
            removeRoomTable(timeTable, roomTable);
        }
        timeTableRepository.delete(timeTable);
    }

    /**
     * Method to load names of all available timeTables for home view
     * @return list of all timeTable names
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<TimeTableNameDTO> loadTimeTableNames(){
        List<TimeTableNameDTO> timeTableNameDTOS = timeTableRepository.findAllTimeTableDTOs();
        log.info("Loaded names of all available timeTables ({})", timeTableNameDTOS.size());
        return timeTableNameDTOS;
    }

    /**
     * Assigns all course sessions that are not assigned yet to room tables within a timetable.
     * This is the place where the algorithm for scheduling course sessions into room tables will be executed.
     *
     * @param timeTable The timetable for executing the algorithm
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public boolean assignCourseSessionsToRooms(TimeTable timeTable){
        log.info("Starting assignment algorithm for timeTable with id {}", timeTable.getId());
        scheduler.setTimeTable(timeTable);
        try{
            scheduler.assignUnassignedCourseSessions();
            timeTableRepository.save(timeTable);
            log.info("Finished assignment algorithm for timeTable with id {}", timeTable.getId());
            globalTableChangeService.create(ChangeType.APPLY_ALGORITHM, timeTable, "Assignment algorithm was processed successfully.");
            return true;
        }
        catch(AssignmentFailedException e){
            log.error("Assignment failed for timeTable with id {}. Unassigning all courseSessions ...", timeTable.getId());
            unassignAllCourseSessions(timeTable);
            return false;
        }
    }

    /**
     * Method to check the current timeTable for collisions
     * @param timeTable to be checked for collisions
     * @return list of colliding courseSessions
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Map<CourseSession,List<CollisionType>> checkCollisions(TimeTable timeTable){
        return scheduler.collisionCheck(timeTable);
    }

    /**
     * Method to unassign all assigned courseSessions of a certain timeTable
     * @param timeTable to unassign all courseSessions of
     * @return updated timeTable
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TimeTable unassignAllCourseSessions(TimeTable timeTable){
        courseSessionService.unassignCourseSessions(timeTable.getAssignedCourseSessions());
        log.info("Unassigned all assigned courseSessions of timeTable {}", timeTable.getId());
        globalTableChangeService.create(ChangeType.CLEAR_TABLE, timeTable, String.format("All assigned courseSessions of timeTable %s %d were unassigned",
                timeTable.getSemester(), timeTable.getYear()));
        timeTable.setRoomTables(roomTableService.loadAllOfTimeTable(timeTable));
        return timeTable;
    }

    /**
     * Method to unassign all courseSessions detected by collision check
     * @param timeTable to unassign colliding courseSessions of
     * @return updated timeTable
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TimeTable unassignCollisions(TimeTable timeTable) {
        List<CourseSession> collisions = scheduler.collisionCheck(timeTable).keySet().stream().toList();
        courseSessionService.unassignCourseSessions(collisions);
        return loadTimeTable(timeTable.getId());
    }

    /**
     * Method to update courseSessions after editor changes. Therefore, the updated courseSessions coming from the frontend
     * are compared to their original versions in the database and changes are detected and updated.
     * @param timeTable to be updated
     * @param courseSessions updated courseSessions from frontend
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void updateCourseSessions(TimeTable timeTable, List<CourseSession> courseSessions){
        List<CourseSession> originalCourseSessions = timeTable.getCourseSessions();
        Map<Long, CourseSession> orig = originalCourseSessions.stream().collect(Collectors.toMap(CourseSession::getId, c -> c));
        for(CourseSession courseSession : courseSessions){
            CourseSession toCompare = orig.get(courseSession.getId());
            if(!toCompare.courseSessionChanged(courseSession)){
                continue;
            }
            courseSessionService.updateCourseSession(timeTable, courseSession, toCompare);
        }
    }

}