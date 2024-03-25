package com.lecture.coordinator.repositories;

import com.lecture.coordinator.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String> {
}
