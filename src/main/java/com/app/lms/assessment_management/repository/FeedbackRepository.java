package com.app.lms.assessment_management.repository;

import com.app.lms.assessment_management.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Feedback findBySubmissionId(Long submissionId);
    List<Feedback> findBySubmissionStudentIdAndSubmissionAssignmentCourseId(Long studentId, Long courseId);

}
