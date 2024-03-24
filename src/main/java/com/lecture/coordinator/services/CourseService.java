package com.lecture.coordinator.services;

import com.lecture.coordinator.model.Course;
import com.lecture.coordinator.model.Timing;
import com.lecture.coordinator.model.TimingTuple;
import com.lecture.coordinator.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

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
    public Course createCourse(String id, String name, String lecturer, int semester, int duration, int numberOfParticipants,
                               int numberOfGroups, boolean isSplit, List<Integer> splitTimes, boolean computersNecessary,
                               boolean isTimingFixed, TimingTuple fixedTimings, List<Timing> timingConstraints) {
        Course course = new Course();
        course.setId(id);
        course.setName(name);
        course.setLecturer(lecturer);
        course.setSemester(semester);
        course.setDuration(duration);
        course.setNumberOfParticipants(numberOfParticipants);
        course.setNumberOfGroups(numberOfGroups);
        course.setSplit(isSplit);
        course.setSplitTimes(splitTimes);
        course.setComputersNecessary(computersNecessary);
        course.setTimingFixed(isTimingFixed);
        course.setFixedTimings(fixedTimings);
        course.setTimingConstraints(timingConstraints);

        return courseRepository.save(course);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteCourse(Course course) {
        courseRepository.delete(course);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteMultipleCourses(List<Course> courses) {
        courseRepository.deleteAll(courses);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Course updateCourse(Course course, String name, String lecturer, int semester, int duration, int numberOfParticipants,
                               int numberOfGroups, boolean isSplit, List<Integer> splitTimes, boolean computersNecessary,
                               boolean isTimingFixed, TimingTuple fixedTimings, List<Timing> timingConstraints) {
        course.setName(name);
        course.setLecturer(lecturer);
        course.setSemester(semester);
        course.setDuration(duration);
        course.setNumberOfParticipants(numberOfParticipants);
        course.setNumberOfGroups(numberOfGroups);
        course.setSplit(isSplit);
        course.setSplitTimes(splitTimes);
        course.setComputersNecessary(computersNecessary);
        course.setTimingFixed(isTimingFixed);
        course.setFixedTimings(fixedTimings);
        course.setTimingConstraints(timingConstraints);

        return courseRepository.save(course);
    }
}
