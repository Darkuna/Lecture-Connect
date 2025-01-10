package at.uibk.leco.repositories;

import at.uibk.leco.models.RoomTable;
import at.uibk.leco.models.Timing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimingRepository extends JpaRepository<Timing, Long> {
    List<Timing> findAllByRoomTable(RoomTable roomTable);
    List<Timing> findAllByCourseId(String courseId);
}
