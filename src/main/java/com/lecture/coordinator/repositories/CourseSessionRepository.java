package com.lecture.coordinator.repositories;

import com.lecture.coordinator.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseSessionRepository extends JpaRepository<CourseSession, String> {
    List<CourseSession> findAllByRoom(Room room);

    @Query("SELECT cs FROM CourseSession cs WHERE cs.isAssigned = false AND cs.timeTable = :timeTable")
    List<CourseSession> findAllByIsAssignedFalseAndTimeTable(@Param("timeTable") TimeTable timeTable);

    @Query("SELECT cs FROM CourseSession cs WHERE cs.course = :course AND cs.timeTable = :timeTable")
    List<CourseSession> findAllByTimeTableAndCourse(@Param("timeTable") TimeTable timeTable,
                                                    @Param("course") Course course);

    @Query("SELECT cs FROM CourseSession cs WHERE cs.timeTable = :timeTable AND cs.roomTable = :roomTable")
    List<CourseSession> findAllByTimeTableAndRoomTable(@Param("timeTable") TimeTable timeTable,
                                                       @Param("roomTable") RoomTable roomTable);
}
