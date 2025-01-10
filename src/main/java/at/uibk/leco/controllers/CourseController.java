package at.uibk.leco.controllers;

import at.uibk.leco.dto.CourseDTO;
import at.uibk.leco.models.Course;
import at.uibk.leco.services.CourseService;
import at.uibk.leco.services.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Scope("session")
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final DTOConverter dtoConverter;

    @Autowired
    public CourseController(CourseService courseService, DTOConverter dtoConverter) {
        this.courseService = courseService;
        this.dtoConverter = dtoConverter;
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDto) {
        Course course = dtoConverter.toCourse(courseDto);
        Course savedCourse = courseService.createCourse(course);
        return ResponseEntity.ok(dtoConverter.toCourseDTO(savedCourse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable String id, @RequestBody CourseDTO courseDto) {
        Course existingCourse = courseService.loadCourseById(id);
        dtoConverter.copyCourseDtoToEntity(courseDto, existingCourse);
        Course updatedCourse = courseService.updateCourse(existingCourse);
        return ResponseEntity.ok(dtoConverter.toCourseDTO(updatedCourse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        Course course = courseService.loadCourseById(id);
        courseService.deleteCourse(course);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMultipleCourses(@RequestBody List<String> courseIds) {
        courseService.deleteMultipleCourses(courseIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<Course> courses = courseService.loadAllCourses();
        return ResponseEntity.ok(courses.stream().map(dtoConverter::toCourseDTO).
                collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable String id) {
        Course course = courseService.loadCourseById(id);
        return ResponseEntity.ok(dtoConverter.toCourseDTO(course));
    }
}

