package com.app.lms.course_management.repository;
import com.app.lms.course_management.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
