package com.lecture.coordinator.services;

import com.lecture.coordinator.constants.TimingConstants;
import com.lecture.coordinator.model.Course;
import com.lecture.coordinator.model.RoomTable;
import com.lecture.coordinator.model.enums.Day;
import java.util.List;

import com.lecture.coordinator.model.Timing;
import com.lecture.coordinator.repositories.TimingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalTime;

/**
 * Service class for managing timings.
 * Provides functionalities to create, update, and retrieve timings, as well as associate them with
 * courses and room tables.
 */
@Service
@Scope("session")
public class TimingService {
    @Autowired
    private TimingRepository timingRepository;

    /**
     * Creates a new timing with specified start time, end time, and day of the week,
     * and saves it to the database.
     *
     * @param startTime The start time of the timing.
     * @param endTime The end time of the timing.
     * @param day The day of the week for the timing.
     * @return The newly created and persisted Timing object.
     * @throws IllegalArgumentException if the start time is after the end time.
     * @throws IllegalArgumentException if the start time is before global start time.
     * @throws IllegalArgumentException if the end time is after global end time.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Timing createTiming(LocalTime startTime, LocalTime endTime, Day day){
        if(startTime.isAfter(endTime)){
            throw new IllegalArgumentException("startTime must be before endTime");
        }
        if(startTime.isBefore(TimingConstants.START_TIME) || endTime.isAfter(TimingConstants.END_TIME)){
            throw new IllegalArgumentException(String.format("startTime cannot be before %s, endTime cannot be after %s",
                    TimingConstants.START_TIME, TimingConstants.END_TIME));
        }
        Timing timing = new Timing();
        timing.setStartTime(startTime);
        timing.setEndTime(endTime);
        timing.setDay(day);
        return timingRepository.save(timing);
    }

    /**
     * Updates an existing timing with new start time, end time, and day of the week.
     *
     * @param timing The Timing object to be updated.
     * @param startTime The new start time for the timing.
     * @param endTime The new end time for the timing.
     * @param day The new day of the week for the timing.
     * @return The updated and persisted Timing object.
     * @throws IllegalArgumentException if the start time is after the end time.
     * @throws IllegalArgumentException if the start time is before global start time.
     * @throws IllegalArgumentException if the end time is after global end time.
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Timing updateTiming(Timing timing, LocalTime startTime, LocalTime endTime, Day day){
        if(startTime.isAfter(endTime)){
            throw new IllegalArgumentException("startTime must be before endTime");
        }
        if(startTime.isBefore(TimingConstants.START_TIME) || endTime.isAfter(TimingConstants.END_TIME)){
            throw new IllegalArgumentException(String.format("startTime cannot be before %s, endTime cannot be after %s",
                    TimingConstants.START_TIME, TimingConstants.END_TIME));
        }
        timing.setStartTime(startTime);
        timing.setEndTime(endTime);
        timing.setDay(day);
        return timingRepository.save(timing);
    }

    /**
     * Loads all timing constraints associated with a specific room table.
     *
     * @param roomTable The room table for which to load timing constraints.
     * @return A list of Timing objects associated with the given room table.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Timing> loadTimingConstraintsOfRoomTable(RoomTable roomTable){
        return timingRepository.findAllByRoomTable(roomTable);
    }

    /**
     * Loads all timing constraints associated with a specific course.
     *
     * @param course The course for which to load timing constraints.
     * @return A list of Timing objects associated with the given course.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Timing> loadTimingConstraintsOfCourse(Course course){
        return timingRepository.findAllByCourse(course);
    }

    /**
     * Loads a specific timing by its ID.
     *
     * @param id The unique identifier of the timing to be loaded.
     * @return The Timing object associated with the given ID.
     * @throws EntityNotFoundException If no timing is found for the provided ID.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Timing loadTimingByID(long id){
        return timingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Timing not found for ID: " + id));
    }

    /**
     * Deletes a list of timing constraints from the database.
     *
     * @param timingConstraints The list of Timing objects to be deleted.
     */
    public void deleteTimingConstraints(List<Timing> timingConstraints){
        timingRepository.deleteAll(timingConstraints);
    }
}
