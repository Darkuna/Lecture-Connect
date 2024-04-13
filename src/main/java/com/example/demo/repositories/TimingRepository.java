package com.example.demo.repositories;

import com.example.demo.models.Course;
import com.example.demo.models.RoomTable;
import com.example.demo.models.Timing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimingRepository extends JpaRepository<Timing, Long> {
    List<Timing> findAllByRoomTable(RoomTable roomTable);
    List<Timing> findAllByCourse(Course course);
}
