package at.uibk.leco.repositories;

import at.uibk.leco.models.RoomTable;
import at.uibk.leco.models.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomTableRepository extends JpaRepository<RoomTable, Long> {
    List<RoomTable> findAllByTimeTable(@Param("timeTable") TimeTable timeTable);
}
