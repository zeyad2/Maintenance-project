package com.app.lms.assessment_management.repository;

import com.app.lms.assessment_management.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    boolean existsByStudentIdAndQuizId(Long userId, Long quizId);
    List<QuizAttempt> findByStudentIdAndQuizCourseId(Long studentId, Long courseId);

}
