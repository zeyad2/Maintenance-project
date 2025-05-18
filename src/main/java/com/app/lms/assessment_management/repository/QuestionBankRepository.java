package com.app.lms.assessment_management.repository;

import com.app.lms.assessment_management.model.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface QuestionBankRepository extends JpaRepository<QuestionBank, Long> {
    Optional<QuestionBank> findByCourseId(Long courseId);
}
