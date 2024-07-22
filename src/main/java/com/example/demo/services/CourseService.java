package com.example.demo.services;

import com.example.demo.models.Course;
import com.example.demo.models.Timing;
import com.example.demo.models.enums.CourseType;
import com.example.demo.repositories.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing courses.
 * Provides functionalities for creating, deleting, updating, and retrieving course information.
 */
@Service
@Scope("session")
public class CourseService {

    private final CourseRepository courseRepository;
    private final TimingService timingService;
    private static final Logger log = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    public CourseService(CourseRepository courseRepository, TimingService timingService){
        this.courseRepository = courseRepository;
        this.timingService = timingService;
    }

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

        course = courseRepository.save(course);
        log.info("Created course with id {}", course.getId());
        return course;
    }

    /**
     * Deletes a course from the database, also deleting all courseSessions that were created from this course.
     *
     * @param course The course to be deleted.
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteCourse(Course course){
        courseRepository.delete(course);
        log.info("Deleted course with id {}", course.getId());
    }

    /**
     * Deletes multiple courses from the database, also deleting all courseSessions that were created from these courses.
     *
     * @param courseIds A list of course ids of courses to be deleted.
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteMultipleCourses(List<String> courseIds) {
        List<Course> coursesToDelete = courseRepository.findAllById(courseIds);
        for(Course course : coursesToDelete){
            deleteCourse(course);
        }
        log.info("Deleted {} courses", coursesToDelete.size());
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
    @Transactional
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
     * Updates an existing course with new details.
     *
     * @param course The updated course to be saved.
     * @return The updated course.
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Course updateCourse(Course course){
        course = courseRepository.save(course);
        log.info("Updated course with id {}", course.getId());
        return course;
    }

    /**
     * Loads a course including its timing constraints.
     *
     * @param id ID of the course to be loaded.
     * @return The course including its timing constraints.
     * @throws EntityNotFoundException if the course could not be found in the database.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Course loadCourseById(String id){
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Course not found for ID: " + id));
        course.setTimingConstraints(timingService.loadTimingConstraintsOfCourse(course));
        log.info("Loaded course with id {}", course.getId());
        return course;
    }

    /**
     * Loads all courses without timing constraints.
     *
     * @return A list of all courses.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Course> loadAllCourses(){
        List<Course> courses = courseRepository.findAll();
        for(Course course : courses){
            course.setTimingConstraints(timingService.loadTimingConstraintsOfCourse(course));
        }
        log.info("Loaded {} courses", courses.size());
        return courses;
    }
}
