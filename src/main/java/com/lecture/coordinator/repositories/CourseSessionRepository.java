package com.lecture.coordinator.repositories;

import com.lecture.coordinator.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseSessionRepository extends JpaRepository<CourseSession, Long> {

    @Query("SELECT cs FROM CourseSession cs WHERE cs.isAssigned = false AND cs.timeTable = :timeTable")
    List<CourseSession> findAllByIsAssignedFalseAndTimeTable(@Param("timeTable") TimeTable timeTable);

    @Query("SELECT cs FROM CourseSession cs WHERE cs.course = :course AND cs.timeTable = :timeTable")
    List<CourseSession> findAllByTimeTableAndCourse(@Param("timeTable") TimeTable timeTable,
                                                    @Param("course") Course course);

    @Query("SELECT cs FROM CourseSession cs WHERE cs.roomTable = :roomTable")
    List<CourseSession> findAllByRoomTable(@Param("roomTable") RoomTable roomTable);

    List<CourseSession> findAllByTimeTable(@Param("timeTable") TimeTable timeTable);

    List<CourseSession> findAllByCourse(@Param("course") Course course);
}
