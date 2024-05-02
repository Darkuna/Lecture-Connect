package com.example.demo.controllers;

import com.example.demo.dto.CourseDTO;
import com.example.demo.dto.TimingDTO;
import com.example.demo.models.Course;
import com.example.demo.models.Timing;
import com.example.demo.models.enums.Day;
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
        Course course = convertToEntity(courseDto);
        Course savedCourse = courseService.createCourse(course);
        return ResponseEntity.ok(convertToDto(savedCourse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable String id, @RequestBody CourseDTO courseDto) {
        Course existingCourse = courseService.loadCourseById(id);
        copyDtoToEntity(courseDto, existingCourse);
        Course updatedCourse = courseService.updateCourse(existingCourse);
        return ResponseEntity.ok(convertToDto(updatedCourse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        Course course = courseService.loadCourseById(id);
        courseService.deleteCourse(course);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<Course> courses = courseService.loadAllCourses();
        return ResponseEntity.ok(courses.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable String id) {
        Course course = courseService.loadCourseById(id);
        return ResponseEntity.ok(convertToDto(course));
    }

    private CourseDTO convertToDto(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setCourseType(course.getCourseType());
        dto.setLecturer(course.getLecturer());
        dto.setSemester(course.getSemester());
        dto.setDuration(course.getDuration());
        dto.setNumberOfParticipants(course.getNumberOfParticipants());
        dto.setComputersNecessary(course.isComputersNecessary());
        dto.setTimingConstraints(course.getTimingConstraints().stream()
                .map(this::convertToTimingDto)  // Annahme, dass convertToTimingDto implementiert ist
                .collect(Collectors.toList()));
        return dto;
    }


    private Course convertToEntity(CourseDTO dto) {
        Course course = new Course();
        course.setId(dto.getId());
        course.setName(dto.getName());
        course.setCourseType(dto.getCourseType());
        course.setLecturer(dto.getLecturer());
        course.setSemester(dto.getSemester());
        course.setDuration(dto.getDuration());
        course.setNumberOfParticipants(dto.getNumberOfParticipants());
        course.setComputersNecessary(dto.isComputersNecessary());
        course.setTimingConstraints(dto.getTimingConstraints().stream()
                .map(this::convertToTimingEntity)
                .collect(Collectors.toList()));
        return course;
    }


    private void copyDtoToEntity(CourseDTO dto, Course entity) {
        entity.setName(dto.getName());
        entity.setCourseType(dto.getCourseType());
        entity.setLecturer(dto.getLecturer());
        entity.setSemester(dto.getSemester());
        entity.setDuration(dto.getDuration());
        entity.setNumberOfParticipants(dto.getNumberOfParticipants());
        entity.setComputersNecessary(dto.isComputersNecessary());
        List<Timing> timings = dto.getTimingConstraints().stream()
                .map(this::convertToTimingEntity)
                .collect(Collectors.toList());
        entity.setTimingConstraints(timings);
    }

    private Timing convertToTimingEntity(TimingDTO dto) {
        Timing timing = new Timing();
        timing.setId(dto.getId());
        timing.setStartTime(dto.getStartTime());
        timing.setEndTime(dto.getEndTime());
        if (dto.getDayOfTheWeek() != null) {
            timing.setDay(Day.valueOf(dto.getDayOfTheWeek()));
        }
        return timing;
    }

    private TimingDTO convertToTimingDto(Timing timing) {
        TimingDTO dto = new TimingDTO();
        dto.setId(timing.getId());
        dto.setStartTime(timing.getStartTime());
        dto.setEndTime(timing.getEndTime());
        dto.setDayOfTheWeek(timing.getDay() != null ? timing.getDay().name() : null);
        return dto;
    }
}

