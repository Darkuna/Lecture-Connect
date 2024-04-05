package com.lecture.coordinator.repositories;

import com.lecture.coordinator.model.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
}
