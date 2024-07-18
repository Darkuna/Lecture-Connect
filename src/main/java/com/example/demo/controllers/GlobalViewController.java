package com.example.demo.controllers;

import com.example.demo.dto.TimeTableCreationDTO;
import com.example.demo.dto.TimeTableDTO;
import com.example.demo.dto.TimeTableNameDTO;
import com.example.demo.models.TimeTable;
import com.example.demo.services.DTOConverter;
import com.example.demo.services.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Scope("session")
@RequestMapping("/api/global")
public class GlobalViewController {
    private final TimeTableService timeTableService;
    private final DTOConverter dtoConverter;

    @Autowired
    public GlobalViewController(TimeTableService timeTableService, DTOConverter dtoConverter) {
        this.timeTableService = timeTableService;
        this.dtoConverter = dtoConverter;
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
        TimeTableDTO timeTableDTO = dtoConverter.toTimeTableDTO(timeTableService.loadTimeTable(id));
        return ResponseEntity.ok(timeTableDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeTable(@PathVariable Long id){
        TimeTable timeTable = timeTableService.loadTimeTable(id);
        timeTableService.deleteTimeTable(timeTable);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<TimeTableDTO> createTimeTable(@RequestBody TimeTableCreationDTO timeTableCreationDTO) {
        TimeTable newTimeTable = timeTableService.createTimeTable(timeTableCreationDTO);
        return ResponseEntity.ok().body(dtoConverter.toTimeTableDTO(newTimeTable));
    }
}
