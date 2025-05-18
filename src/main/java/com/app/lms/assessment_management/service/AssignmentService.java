package com.app.lms.assessment_management.service;

import com.app.lms.assessment_management.model.Assignment;
import com.app.lms.assessment_management.repository.AssignmentRepository;
import com.app.lms.assessment_management.repository.SubmissionRepository;
import com.app.lms.course_management.model.Course;
import com.app.lms.course_management.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final SubmissionRepository submissionRepository;

    public AssignmentService(AssignmentRepository assignmentRepository, CourseRepository courseRepository, SubmissionRepository submissionRepository) {
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
        this.submissionRepository = submissionRepository;
    }

    public Assignment createAssignment(Assignment assignment, Long courseId, MultipartFile file) {
        // Find the course by ID
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));

        assignment.setCourse(course);

        // Save the assignment to get its ID
        Assignment savedAssignment = assignmentRepository.save(assignment);

        // Define file upload path
        String uploadDir = System.getProperty("user.dir") + "/uploads/courses/" + courseId + "/assignments/" + savedAssignment.getId();
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save file to the directory
        String filePath = uploadDir + "/" + file.getOriginalFilename();
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage());
        }

        // Update the assignment with the file path
        savedAssignment.setFilePath(filePath);
        return assignmentRepository.save(savedAssignment);
    }

    public List<Assignment> getAllAssignments(Long courseId) {
        return assignmentRepository.findByCourseId(courseId);
    }

    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found for ID: " + id));
    }

}
