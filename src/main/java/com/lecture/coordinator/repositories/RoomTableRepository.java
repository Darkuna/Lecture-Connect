package com.lecture.coordinator.repositories;

import com.lecture.coordinator.model.RoomTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTableRepository extends JpaRepository<RoomTable, Long> {
}
