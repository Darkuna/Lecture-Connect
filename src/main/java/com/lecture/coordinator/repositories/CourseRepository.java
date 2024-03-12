package com.lecture.coordinator.repositories;

import com.lecture.coordinator.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, String> {
}
