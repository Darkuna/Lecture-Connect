package com.example.demo.controllers;

import com.example.demo.dto.CourseSessionDTO;
import com.example.demo.models.CourseSession;
import com.example.demo.services.CourseSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Scope("session")
@RequestMapping("/api/courseSessions")
public class CourseSessionController {
    private final CourseSessionService courseSessionService;

    @Autowired
    public CourseSessionController(CourseSessionService courseSessionService) {
        this.courseSessionService = courseSessionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseSessionDTO> getCourseSessionById(@PathVariable Long id) {
        CourseSession courseSession = courseSessionService.loadCourseSessionByID(id);
        return ResponseEntity.ok(courseSessionService.toDTO(courseSession));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseSession(@PathVariable Long id) {
        CourseSession courseSession = courseSessionService.loadCourseSessionByID(id);
        courseSessionService.deleteCourseSession(courseSession);
        return ResponseEntity.ok().build();
    }
}
