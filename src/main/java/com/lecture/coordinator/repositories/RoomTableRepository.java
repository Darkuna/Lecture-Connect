package com.lecture.coordinator.repositories;

import com.lecture.coordinator.model.Room;
import com.lecture.coordinator.model.RoomTable;
import com.lecture.coordinator.model.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomTableRepository extends JpaRepository<RoomTable, Long> {
    RoomTable findRoomTableByRoom(@Param("room") Room room);

    List<RoomTable> findAllByTimeTable(@Param("timeTable")TimeTable timeTable);
}
