package com.lecture.coordinator.services;

import com.lecture.coordinator.model.Course;
import com.lecture.coordinator.model.CourseSession;
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

@Service
@Scope("session")
public class TimingService {
    @Autowired
    private TimingRepository timingRepository;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Timing createTiming(LocalTime startTime, LocalTime endTime, Day day){
        if(startTime.isAfter(endTime)){
            throw new IllegalArgumentException("startTime must be before endTime");
        }
        Timing timing = new Timing();
        timing.setStartTime(startTime);
        timing.setEndTime(endTime);
        timing.setDay(day);
        return timingRepository.save(timing);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Timing updateTiming(Timing timing, LocalTime startTime, LocalTime endTime, Day day){
        if(startTime.isAfter(endTime)){
            throw new IllegalArgumentException("startTime must be before endTime");
        }
        timing.setStartTime(startTime);
        timing.setEndTime(endTime);
        timing.setDay(day);
        return timingRepository.save(timing);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Timing> loadTimingConstraintsOfRoomTable(RoomTable roomTable){
        return timingRepository.findAllByRoomTable(roomTable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Timing> loadTimingConstraintsOfCourse(Course course){
        return timingRepository.findAllByCourse(course);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Timing loadTimingByID(long id){
        return timingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Timing not found for ID: " + id));
    }

    public void deleteTimingConstraints(List<Timing> timingConstraints){
        timingRepository.deleteAll(timingConstraints);
    }
}
