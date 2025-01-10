package at.uibk.leco.repositories;


import at.uibk.leco.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, String> {
}
