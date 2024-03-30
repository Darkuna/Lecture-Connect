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

/**
 * Service class for managing courses.
 * Provides functionalities for creating, deleting, updating, and retrieving course information.
 */
@Service
@Scope("session")
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TimingService timingService;
    @Autowired
    private CourseSessionService courseSessionService;

    /**
     * Creates a new course and saves it to the database.
     *
     * @param course The course object to be saved.
     * @return The saved course.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    /**
     * Creates a new course with provided details and saves it to the database.
     *
     * @param id The ID of the course.
     * @param name The name of the course.
     * @param courseType The type of the course.
     * @param lecturer The lecturer's name.
     * @param semester The semester when the course is offered.
     * @param duration The duration of the course in minutes.
     * @param numberOfParticipants The number of participants allowed in the course.
     * @param computersNecessary Boolean indicating if computers are necessary for the course.
     * @param timingConstraints A list of timing constraints for the course.
     * @return The saved course.
     */
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

    /**
     * Deletes a course from the database, also deleting all courseSessions that were created from this course.
     *
     * @param course The course to be deleted.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteCourse(Course course){
        List<CourseSession> courseSessions = courseSessionService.loadAllFromCourse(course);
        for(CourseSession courseSession : courseSessions){
            courseSessionService.deleteCourseSession(courseSession);
        }
        courseRepository.delete(course);
    }

    /**
     * Deletes multiple courses from the database, also deleting all courseSessions that were created from these courses.
     *
     * @param courses A list of courses to be deleted.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteMultipleCourses(List<Course> courses) {
        for(Course course : courses){
            deleteCourse(course);
        }
    }

    /**
     * Updates an existing course with new details.
     *
     * @param course The course to be updated.
     * @param name New name for the course.
     * @param courseType New type for the course.
     * @param lecturer New lecturer's name.
     * @param semester New semester when the course is offered.
     * @param duration New duration of the course in minutes.
     * @param numberOfParticipants New number of participants allowed in the course.
     * @param computersNecessary New boolean value indicating if computers are necessary for the course.
     * @param timingConstraints New list of timing constraints for the course.
     * @return The updated course.
     */
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

    /**
     * Loads a course including its timing constraints.
     *
     * @param id ID of the course to be loaded.
     * @return The course including its timing constraints.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Course loadCourseById(String id){
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Course not found for ID: " + id));
        course.setTimingConstraints(timingService.loadTimingConstraintsOfCourse(course));
        return course;
    }

    /**
     * Loads all courses without timing constraints.
     *
     * @return A list of all courses.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Course> loadAllCourses(){
        return courseRepository.findAll();
    }
}
