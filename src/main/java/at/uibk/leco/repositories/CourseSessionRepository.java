package at.uibk.leco.repositories;

import at.uibk.leco.models.CourseSession;
import at.uibk.leco.models.RoomTable;
import at.uibk.leco.models.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseSessionRepository extends JpaRepository<CourseSession, Long> {

    @Query("SELECT cs FROM CourseSession cs WHERE cs.roomTable = :roomTable")
    List<CourseSession> findAllByRoomTable(@Param("roomTable") RoomTable roomTable);

    List<CourseSession> findAllByTimeTable(@Param("timeTable") TimeTable timeTable);

    CourseSession findFirstByCourseIdAndTimeTable(String courseId, TimeTable timeTable);

}
