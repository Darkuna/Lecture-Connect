package com.example.demo.repositories;

import com.example.demo.models.GlobalTableChange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GlobalTableChangeRepository extends JpaRepository<GlobalTableChange, Long> {
    List<GlobalTableChange> findAllByTimeTable(String timeTable);
}
