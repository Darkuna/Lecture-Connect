package com.lecture.coordinator.repositories;

import com.lecture.coordinator.model.Course;
import com.lecture.coordinator.model.RoomTable;
import com.lecture.coordinator.model.Timing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimingRepository extends JpaRepository<Timing, Long> {
    List<Timing> findAllByRoomTable(RoomTable roomTable);
    List<Timing> findAllByCourse(Course course);
}
