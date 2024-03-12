package com.lecture.coordinator.repositories;

import com.lecture.coordinator.model.CourseSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseSessionRepository extends JpaRepository<CourseSession, String> {
}
