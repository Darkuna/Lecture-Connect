package com.example.demo.repositories;

import com.example.demo.dto.TimeTableNameDTO;
import com.example.demo.models.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
    @Query("SELECT new com.example.demo.dto.TimeTableNameDTO(t.id, t.semester, t.year) FROM TimeTable t")
    List<TimeTableNameDTO> findAllTimeTableDTOs();
}
