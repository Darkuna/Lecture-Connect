package com.example.demo.repositories;

import com.example.demo.models.Room;
import com.example.demo.models.RoomTable;
import com.example.demo.models.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomTableRepository extends JpaRepository<RoomTable, Long> {
    List<RoomTable> findAllByRoom(@Param("room") Room room);

    List<RoomTable> findAllByTimeTable(@Param("timeTable") TimeTable timeTable);
}
