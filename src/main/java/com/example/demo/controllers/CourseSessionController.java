package com.example.demo.controllers;

import com.example.demo.dto.CourseSessionDTO;
import com.example.demo.models.CourseSession;
import com.example.demo.services.CourseSessionService;
import com.example.demo.services.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Scope("session")
@RequestMapping("/api/courseSessions")
public class CourseSessionController {
    private final CourseSessionService courseSessionService;
    private final DTOConverter dtoConverter;

    @Autowired
    public CourseSessionController(CourseSessionService courseSessionService, DTOConverter dtoConverter) {
        this.courseSessionService = courseSessionService;
        this.dtoConverter = dtoConverter;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseSessionDTO> getCourseSessionById(@PathVariable Long id) {
        CourseSession courseSession = courseSessionService.loadCourseSessionByID(id);
        return ResponseEntity.ok(dtoConverter.toCourseSessionDTO(courseSession));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseSession(@PathVariable Long id) {
        CourseSession courseSession = courseSessionService.loadCourseSessionByID(id);
        courseSessionService.deleteCourseSession(courseSession);
        return ResponseEntity.ok().build();
    }
}
