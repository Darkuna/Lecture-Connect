package at.uibk.leco.repositories;

import at.uibk.leco.dto.TimeTableNameDTO;
import at.uibk.leco.models.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import at.uibk.leco.dto.TimeTableNameDTO;

import java.util.List;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
    @Query("SELECT new at.uibk.leco.dto.TimeTableNameDTO(t.id, t.semester, t.year, t.name) FROM TimeTable t")
    List<TimeTableNameDTO> findAllTimeTableDTOs();
}
