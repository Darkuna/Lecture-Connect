package com.example.demo.controllers;

import com.example.demo.dto.CourseDTO;
import com.example.demo.models.Course;
import com.example.demo.services.CourseService;
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

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDto) {
        Course course = courseService.fromDTO(courseDto);
        Course savedCourse = courseService.createCourse(course);
        return ResponseEntity.ok(courseService.toDTO(savedCourse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable String id, @RequestBody CourseDTO courseDto) {
        Course existingCourse = courseService.loadCourseById(id);
        courseService.copyDtoToEntity(courseDto, existingCourse);
        Course updatedCourse = courseService.updateCourse(existingCourse);
        return ResponseEntity.ok(courseService.toDTO(updatedCourse));
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
        return ResponseEntity.ok(courses.stream().map(courseService::toDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable String id) {
        Course course = courseService.loadCourseById(id);
        return ResponseEntity.ok(courseService.toDTO(course));
    }
}

