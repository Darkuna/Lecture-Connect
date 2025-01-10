package at.uibk.leco.repositories;

import at.uibk.leco.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String> {
}
