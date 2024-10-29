package com.example.demo.controllers;

import com.example.demo.dto.*;
import com.example.demo.models.CourseSession;
import com.example.demo.models.TimeTable;
import com.example.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    private TimeTable timeTable;

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
        timeTable = timeTableService.loadTimeTable(id);
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

        if (timeTableService.assignCourseSessionsToRooms(timeTable)) {
            TimeTableDTO updatedTimeTableDTO = dtoConverter.toTimeTableDTO(timeTable);
            return ResponseEntity.ok().body(updatedTimeTableDTO);
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    @PostMapping("/assignment/remove/{id}")
    public ResponseEntity<TimeTableDTO> removeAllAssignedCourseSessionsFromTimeTable(@PathVariable Long id) {
        timeTable = timeTableService.unassignAllCourseSessions(timeTable);
        TimeTableDTO updatedTimeTableDTO = dtoConverter.toTimeTableDTO(timeTable);
        return ResponseEntity.ok().body(updatedTimeTableDTO);
    }

    @PostMapping("/assignment/removeCollisions/{id}")
    public ResponseEntity<TimeTableDTO> removeCollisionsFromTimeTable(@PathVariable Long id) {
        timeTable = timeTableService.unassignCollisions(timeTable);
        TimeTableDTO updatedTimeTableDTO = dtoConverter.toTimeTableDTO(timeTable);
        return ResponseEntity.ok().body(updatedTimeTableDTO);
    }

    @PostMapping("/collision/{id}")
    public ResponseEntity<List<CourseSessionDTO>> checkCollision(@PathVariable Long id) {
        TimeTable timeTable = timeTableService.loadTimeTable(id);
        //TODO: the new collision check return a Map<CourseSession, List<CollisionType>. Make use of that
        List<CourseSession> collisions = timeTableService.checkCollisions(timeTable).keySet().stream().toList();
        List<CourseSessionDTO> collisionDTOs = collisions.stream()
                .map(dtoConverter::toCourseSessionDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(collisionDTOs);
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
    public ResponseEntity<Void> addCoursesToTimeTable(@PathVariable Long id, @RequestBody List<CourseDTO> courseDTOs) {
        TimeTable timeTable = timeTableService.loadTimeTable(id);
        for(CourseDTO courseDTO : courseDTOs) {
            timeTableService.createCourseSessions(timeTable, dtoConverter.toCourse(courseDTO));
        }
        return ResponseEntity.ok().build();
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

    @GetMapping("/changes")
    public ResponseEntity<List<GlobalTableChangeDTO>> getGlobalTableChanges() {
        List<GlobalTableChangeDTO> globalTableChanges = globalTableChangeService.loadAll().stream()
                .map(dtoConverter::toGlobalTableChangeDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(globalTableChanges);
    }
}
