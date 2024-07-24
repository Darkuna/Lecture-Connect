package com.example.demo.services;

import com.example.demo.constants.TimingConstants;
import com.example.demo.models.RoomTable;
import com.example.demo.models.Timing;
import com.example.demo.models.enums.Day;
import com.example.demo.models.enums.TimingType;
import com.example.demo.repositories.TimingRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

/**
 * Service class for managing timings.
 * Provides functionalities to create, update, and retrieve timings, as well as associate them with
 * courses and room tables.
 */
@Service
@Scope("session")
public class TimingService {
    private final TimingRepository timingRepository;
    private static final Logger log = LoggerFactory.getLogger(TimingService.class);

    public TimingService(TimingRepository timingRepository) {
        this.timingRepository = timingRepository;
    }
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
    public Timing createTiming(LocalTime startTime, LocalTime endTime, Day day, TimingType timingType) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("startTime must be before endTime");
        }
        if (startTime.isBefore(TimingConstants.START_TIME) || endTime.isAfter(TimingConstants.END_TIME)) {
            throw new IllegalArgumentException(String.format("startTime cannot be before %s, endTime cannot be after %s",
                    TimingConstants.START_TIME, TimingConstants.END_TIME));
        }
        Timing timing = new Timing();
        timing.setStartTime(startTime);
        timing.setEndTime(endTime);
        timing.setDay(day);
        timing.setTimingType(timingType);
        timing = timingRepository.save(timing);
        log.debug("Created timing with id {}", timing.getId());
        return timing;
    }

    /**
     * Creates a new Timing object from a transient timing object
     *
     * @param timing to be persisted
     * @return persisted timing object
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Timing createTiming(Timing timing){
        timing.setId(null);
        if(timing.getStartTime().isAfter(timing.getEndTime())){
            throw new IllegalArgumentException("startTime must be before endTime");
        }
        if(timing.getStartTime().isBefore(TimingConstants.START_TIME) || timing.getEndTime().isAfter(TimingConstants.END_TIME)){
            throw new IllegalArgumentException(String.format("startTime cannot be before %s, endTime cannot be after %s",
                    TimingConstants.START_TIME, TimingConstants.END_TIME));
        }
        timing = timingRepository.save(timing);
        log.debug("Created timing with id {}", timing.getId());
        return timing;
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
    public Timing updateTiming(Timing timing, LocalTime startTime, LocalTime endTime, Day day, TimingType timingType){
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
        timing.setTimingType(timingType);
        log.info("Updated timing with id {}", timing.getId());
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
        List<Timing> timingConstraints = timingRepository.findAllByRoomTable(roomTable);
        log.info("Loaded all timingConstraints of roomTable {} ({})", roomTable.getId(), timingConstraints.size());
        return timingConstraints;
    }

    /**
     * Loads all timing constraints associated with a specific course.
     *
     * @param courseId The courseId of the course to load timing constraints of.
     * @return A list of Timing objects associated with the given course.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Timing> loadTimingConstraintsOfCourse(String courseId){
        List<Timing> timingConstraints = timingRepository.findAllByCourseId(courseId);
        log.debug("Loaded all timingConstraints of course {} ({})", courseId, timingConstraints.size());
        return timingConstraints;
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
        Timing timing = timingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Timing not found for ID: " + id));
        log.info("Loaded timing with id {}", id);
        return timing;
    }

    /**
     * Deletes a list of timing constraints from the database.
     *
     * @param timingConstraints The list of Timing objects to be deleted.
     */
    public void deleteTimingConstraints(List<Timing> timingConstraints){
        if(timingConstraints != null){
            timingRepository.deleteAll(timingConstraints);
            log.info("Deleted {} timingConstraints", timingConstraints.size());
        }
    }

    /**
     * Deletes a timing from the database.
     *
     * @param timing The Timing object to be deleted.
     */
    public void deleteTiming(Timing timing){
        timingRepository.delete(timing);
        log.info("Deleted timing with id {}", timing.getId());
    }

}
