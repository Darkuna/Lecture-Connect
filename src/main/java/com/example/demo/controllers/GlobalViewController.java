package com.example.demo.controllers;

import com.example.demo.dto.*;
import com.example.demo.models.Course;
import com.example.demo.models.CourseSession;
import com.example.demo.models.Room;
import com.example.demo.models.TimeTable;
import com.example.demo.services.CourseService;
import com.example.demo.services.DTOConverter;
import com.example.demo.services.RoomService;
import com.example.demo.services.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
    private TimeTable timeTable;

    @Autowired
    public GlobalViewController(TimeTableService timeTableService, DTOConverter dtoConverter, RoomService roomService,
                                CourseService courseService) {
        this.timeTableService = timeTableService;
        this.dtoConverter = dtoConverter;
        this.roomService = roomService;
        this.courseService = courseService;
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
        timeTableService.assignCourseSessionsToRooms(timeTable);
        TimeTableDTO updatedTimeTableDTO = dtoConverter.toTimeTableDTO(timeTable);
        return ResponseEntity.ok().body(updatedTimeTableDTO);
    }

    @PostMapping("/collision/{id}")
    public ResponseEntity<List<CourseSessionDTO>> checkCollision(@PathVariable Long id) {
        TimeTable timeTable = timeTableService.loadTimeTable(id);
        List<CourseSession> collisions = timeTableService.checkCollisions(timeTable);
        List<CourseSessionDTO> collisionDTOs = collisions.stream()
                .map(dtoConverter::toCourseSessionDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(collisionDTOs);
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<List<Course>> getCoursesNotInTimeTable(@PathVariable Long id) {
        TimeTable timeTable = timeTableService.loadTimeTable(id);
        List<Course> courses = courseService.loadAllCoursesNotInTimeTable(timeTable);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<List<Room>> getRoomsNotInTimeTable(@PathVariable Long id) {
        TimeTable timeTable = timeTableService.loadTimeTable(id);
        List<Room> rooms = roomService.loadAllRoomsNotInTimeTable(timeTable);
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/add-courses-to-timetable")
    public ResponseEntity<Void> addCoursesToTimeTable(@RequestParam Long timeTableId, @RequestBody List<CourseDTO> courseDTOs) {
        TimeTable timeTable = timeTableService.loadTimeTable(timeTableId);
        for(CourseDTO courseDTO : courseDTOs) {
            timeTableService.createCourseSessions(timeTable, dtoConverter.toCourse(courseDTO));
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-rooms-to-timetable")
    public ResponseEntity<Void> addRoomsToTimeTable(@RequestParam Long timeTableId, @RequestBody List<RoomDTO> roomsDTOs) {
        TimeTable timeTable = timeTableService.loadTimeTable(timeTableId);
        for(RoomDTO roomDTO : roomsDTOs) {
            timeTableService.createRoomTable(timeTable, dtoConverter.toRoom(roomDTO));
        }
        return ResponseEntity.ok().build();
    }
}
