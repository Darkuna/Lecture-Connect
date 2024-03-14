package com.lecture.coordinator.services;

import com.lecture.coordinator.model.*;
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
    @Autowired
    private CourseSessionService courseSessionService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Course createCourse(String id, String name, String lecturer, int duration, int numberOfParticipants,
                               int numberOfGroups, boolean isSplit, IntegerTuple splitTimes, boolean computersNecessary,
                               boolean isTimingFixed, TimingTuple fixedTimings, List<Timing> timingConstraints){
        Course course = new Course();
        course.setId(id);
        course.setName(name);
        course.setLecturer(lecturer);
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
    public void deleteCourse(Course course){
        courseRepository.delete(course);
    }
}
