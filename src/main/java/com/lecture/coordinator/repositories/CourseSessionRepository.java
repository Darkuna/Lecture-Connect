package com.lecture.coordinator.repositories;

import com.lecture.coordinator.model.CourseSession;
import com.lecture.coordinator.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.lecture.coordinator.model.TimeTable;

import java.util.List;

public interface CourseSessionRepository extends JpaRepository<CourseSession, String> {
    List<CourseSession> findAllByRoom(Room room);

    @Query("SELECT cs FROM CourseSession cs WHERE cs.isAssigned = false AND cs.timeTable = :timeTable")
    List<CourseSession> findAllByIsAssignedFalseAndTimeTable(@Param("timeTable") TimeTable timeTable);
}
