package com.app.lms.assessment_management.repository;

import com.app.lms.assessment_management.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByAssignmentId(Long assignmentId);
    List<Submission> findByStudent_Id(Long studentId);

}
