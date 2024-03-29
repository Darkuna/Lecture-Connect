package com.lecture.coordinator.services;

import com.lecture.coordinator.model.*;
import com.lecture.coordinator.model.enums.CourseType;
import com.lecture.coordinator.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Scope("session")
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Course createCourse(String id, String name, CourseType courseType, String lecturer, int semester, int duration,
                               int numberOfParticipants, boolean computersNecessary, List<Timing> timingConstraints){
        Course course = new Course();
        course.setId(id);
        course.setName(name);
        course.setCourseType(courseType);
        course.setLecturer(lecturer);
        course.setSemester(semester);
        course.setDuration(duration);
        course.setNumberOfParticipants(numberOfParticipants);
        course.setComputersNecessary(computersNecessary);
        course.setTimingConstraints(timingConstraints);

        return courseRepository.save(course);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteCourse(Course course){
        courseRepository.delete(course);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteMultipleCourses(List<Course> courses) {
        courseRepository.deleteAll(courses);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Course updateCourse(Course course, String name, CourseType courseType, String lecturer, int semester, int duration,
                               int numberOfParticipants, boolean computersNecessary, List<Timing> timingConstraints){
        course.setName(name);
        course.setCourseType(courseType);
        course.setLecturer(lecturer);
        course.setSemester(semester);
        course.setDuration(duration);
        course.setNumberOfParticipants(numberOfParticipants);
        course.setComputersNecessary(computersNecessary);
        course.setTimingConstraints(timingConstraints);

        return courseRepository.save(course);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Course loadCourseById(String id){
        return courseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Course not found for ID: " + id));
    }
}
