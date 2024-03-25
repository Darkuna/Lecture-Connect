package com.lecture.coordinator.repositories;

import com.lecture.coordinator.model.Course;
import com.lecture.coordinator.model.Room;
import com.lecture.coordinator.model.Timing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimingRepository extends JpaRepository<Timing, Long> {
    public List<Timing> findAllByRoom(Room room);
    public List<Timing> findAllByCourse(Course course);
}
