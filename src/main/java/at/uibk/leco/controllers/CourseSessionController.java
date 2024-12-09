package at.uibk.leco.controllers;

import at.uibk.leco.dto.CourseSessionDTO;
import at.uibk.leco.models.CourseSession;
import at.uibk.leco.services.CourseSessionService;
import at.uibk.leco.services.DTOConverter;
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
