package com.lecture.coordinator.repositories;

import com.lecture.coordinator.model.Timing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimingRepository extends JpaRepository<Timing, Long> {
}
