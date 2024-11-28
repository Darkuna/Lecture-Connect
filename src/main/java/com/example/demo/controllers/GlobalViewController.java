package com.example.demo.controllers;

import com.example.demo.dto.*;
import com.example.demo.exceptions.scheduler.AssignmentFailedException;
import com.example.demo.exceptions.scheduler.NoCandidatesForCourseSessionException;
import com.example.demo.exceptions.scheduler.PreconditionFailedException;
import com.example.demo.models.Course;
import com.example.demo.models.CourseSession;
import com.example.demo.models.TimeTable;
import com.example.demo.scheduling.CollisionType;
import com.example.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Scope("session")
@RequestMapping("/api/global")
public class GlobalViewController {
    private final TimeTableService timeTableService;
    private final DTOConverter dtoConverter;
    private final RoomService roomService;
    private final CourseService courseService;
    private final GlobalTableChangeService globalTableChangeService;


    @Autowired
    public GlobalViewController(TimeTableService timeTableService, DTOConverter dtoConverter, RoomService roomService,
                                CourseService courseService, GlobalTableChangeService globalTableChangeService) {
        this.timeTableService = timeTableService;
        this.dtoConverter = dtoConverter;
        this.roomService = roomService;
        this.courseService = courseService;
        this.globalTableChangeService = globalTableChangeService;
    }

    /**
     * Loads a list of timeTable DTOs that only contain the id, semester and year of each timetable.
     * @return List of TimeTableNameDTO objects
     */
    @GetMapping("/names")
    public ResponseEntity<List<TimeTableNameDTO>> getAllTimeTableDTOs() {
        List<TimeTableNameDTO> timeTableDTOs = timeTableService.loadTimeTableNames();
        return ResponseEntity.ok(timeTableDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeTableDTO> getTimeTableById(@PathVariable Long id){
        TimeTable timeTable = timeTableService.loadTimeTable(id);
        TimeTableDTO timeTableDTO = dtoConverter.toTimeTableDTO(timeTable);
        return ResponseEntity.ok(timeTableDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeTable(@PathVariable Long id){
        TimeTable timeTable = timeTableService.loadTimeTable(id);
        timeTableService.deleteTimeTable(timeTable);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createTimeTable(@RequestBody TimeTableCreationDTO timeTableCreationDTO) {
        TimeTable newTimeTable = timeTableService.createTimeTable(timeTableCreationDTO);
        return ResponseEntity.ok().body(newTimeTable.getId());
    }

    @PostMapping("/assignment/{id}")
    public ResponseEntity<TimeTableDTO> calculateAndUpdateTimeTable(@PathVariable Long id) {
        TimeTable timeTable = timeTableService.loadTimeTable(id);

        if (timeTable == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            timeTableService.assignCourseSessionsToRooms(timeTable);
            TimeTableDTO updatedTimeTableDTO = dtoConverter.toTimeTableDTO(timeTable);
            return ResponseEntity.ok().body(updatedTimeTableDTO);
        } catch (PreconditionFailedException | NoCandidatesForCourseSessionException | AssignmentFailedException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error occurred during assignment processing", ex);
        }
    }


    @PostMapping("/assignment/remove/{id}")
    public ResponseEntity<TimeTableDTO> removeAllAssignedCourseSessionsFromTimeTable(@PathVariable Long id) {
        TimeTable timeTable = timeTableService.loadTimeTable(id);
        timeTable = timeTableService.unassignAllCourseSessions(timeTable);
        TimeTableDTO updatedTimeTableDTO = dtoConverter.toTimeTableDTO(timeTable);
        return ResponseEntity.ok().body(updatedTimeTableDTO);
    }

    @PostMapping("/assignment/removeCollisions/{id}")
    public ResponseEntity<TimeTableDTO> removeCollisionsFromTimeTable(@PathVariable Long id) {
        TimeTable timeTable = timeTableService.loadTimeTable(id);
        timeTable = timeTableService.unassignCollisions(timeTable);
        TimeTableDTO updatedTimeTableDTO = dtoConverter.toTimeTableDTO(timeTable);
        return ResponseEntity.ok().body(updatedTimeTableDTO);
    }

    @PostMapping("/collision/{id}")
    public ResponseEntity<Map<String, List<CollisionType>>> checkCollision(@PathVariable Long id) {
        TimeTable timeTable = timeTableService.loadTimeTable(id);
        Map<CourseSession, List<CollisionType>> collisions = timeTableService.checkCollisions(timeTable);
        Map<String, List<CollisionType>> dtoCollisions = collisions.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getName(),
                        Map.Entry::getValue
                ));
        return ResponseEntity.ok(dtoCollisions);
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<List<CourseDTO>> getCoursesNotInTimeTable(@PathVariable Long id) {
        TimeTable timeTable = timeTableService.loadTimeTable(id);
        List<CourseDTO> courses = courseService.loadAllCoursesNotInTimeTable(timeTable).stream()
                .map(dtoConverter::toCourseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<List<RoomDTO>> getRoomsNotInTimeTable(@PathVariable Long id) {
        TimeTable timeTable = timeTableService.loadTimeTable(id);
        List<RoomDTO> rooms = roomService.loadAllRoomsNotInTimeTable(timeTable).stream()
                .map(dtoConverter::toRoomDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/add-courses-to-timetable/{id}")
    public ResponseEntity<List<CourseSessionDTO>> addCoursesToTimeTable(@PathVariable Long id, @RequestBody List<CourseDTO> courseDTOs) {
        TimeTable timeTable = timeTableService.loadTimeTable(id);
        List<CourseSession> courseSessions = new ArrayList<>();
        for(CourseDTO courseDTO : courseDTOs) {
            Course course = dtoConverter.toCourse(courseDTO);
            course.setNumberOfGroups(1);
            course.setSplit(false);
            courseSessions.addAll(timeTableService.createCourseSessions(timeTable, course));
        }
        List<CourseSessionDTO> courseSessionDTOs = courseSessions.stream()
                .map(dtoConverter::toCourseSessionDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(courseSessionDTOs);
    }

    @PostMapping("/add-rooms-to-timetable/{id}")
    public ResponseEntity<Void> addRoomsToTimeTable(@PathVariable Long id, @RequestBody List<RoomDTO> roomsDTOs) {
        TimeTable timeTable = timeTableService.loadTimeTable(id);
        for(RoomDTO roomDTO : roomsDTOs) {
            timeTableService.createRoomTable(timeTable, dtoConverter.toRoom(roomDTO));
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-course-sessions/{id}")
    public ResponseEntity<Void> updateCourseSessionsOfTimeTable(@PathVariable Long id, @RequestBody List<CourseSessionDTO> courseSessionDTOs) {
        TimeTable timeTable = timeTableService.loadTimeTable(id);
        List<CourseSession> courseSessions = courseSessionDTOs.stream()
                .map(dtoConverter::toCourseSession)
                .collect(Collectors.toList());
        timeTableService.updateCourseSessions(timeTable, courseSessions);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/changes/{id}")
    public ResponseEntity<List<GlobalTableChangeDTO>> getGlobalTableChanges(@PathVariable Long id) {
        TimeTable timeTable = timeTableService.loadTimeTable(id);
        List<GlobalTableChangeDTO> globalTableChanges = globalTableChangeService.loadAllByTimeTable(timeTable).stream()
                .map(dtoConverter::toGlobalTableChangeDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(globalTableChanges);
    }
}
