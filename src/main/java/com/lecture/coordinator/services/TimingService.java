package com.lecture.coordinator.services;

import com.lecture.coordinator.model.Course;
import com.lecture.coordinator.model.Day;
import java.util.List;

import com.lecture.coordinator.model.Room;
import com.lecture.coordinator.model.Timing;
import com.lecture.coordinator.repositories.TimingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@Scope("session")
public class TimingService {
    @Autowired
    private TimingRepository timingRepository;
    public Timing createTiming(LocalTime startTime, LocalTime endTime, Day day){
        Timing timing = new Timing();
        timing.setStartTime(startTime);
        timing.setEndTime(endTime);
        timing.setDay(day);
        return timingRepository.save(timing);
    }

    public List<Timing> loadTimingConstraintsOfRoom(Room room){
        return timingRepository.findAllByRoom(room);
    }

    public List<Timing> loadTimingConstraintsOfCourse(Course course){
        return timingRepository.findAllByCourse(course);
    }
}
