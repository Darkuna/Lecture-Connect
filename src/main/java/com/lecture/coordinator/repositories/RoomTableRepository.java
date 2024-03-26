package com.lecture.coordinator.repositories;

import com.lecture.coordinator.model.Room;
import com.lecture.coordinator.model.RoomTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface RoomTableRepository extends JpaRepository<RoomTable, Long> {
    RoomTable findRoomTableByRoom(@Param("room") Room room);
}
