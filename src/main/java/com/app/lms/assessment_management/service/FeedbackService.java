package com.app.lms.assessment_management.service;

import com.app.lms.assessment_management.model.Feedback;
import com.app.lms.assessment_management.model.Submission;
import com.app.lms.assessment_management.repository.FeedbackRepository;
import com.app.lms.assessment_management.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import com.app.lms.dto.FeedbackRequest;



@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final SubmissionRepository submissionRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, SubmissionRepository submissionRepository) {
        this.feedbackRepository = feedbackRepository;
        this.submissionRepository = submissionRepository;
    }

    public Feedback giveFeedback(FeedbackRequest request) {
        // Check if submission exists
        Submission submission = submissionRepository.findById(request.getSubmissionID()).orElseThrow(() -> new IllegalArgumentException("Submission not found"));

        // Check if feedback already exists
        if (feedbackRepository.findBySubmissionId(request.getSubmissionID()) != null) {
            throw new IllegalStateException("Feedback already provided for this submission");
        }

        // Create feedback
        Feedback feedback = new Feedback();
        feedback.setSubmission(submission);
        feedback.setComments(request.getComment());
        feedback.setGrade(request.getGrade());

        return feedbackRepository.save(feedback);
    }

    public Feedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id).orElseThrow(() -> new RuntimeException("Feedback with ID " + id + " not found"));
    }

    public FeedbackRequest getFeedbackBySubmission(Long submissionId) {
        // 1. Verify the submission exists
        if (!submissionRepository.existsById(submissionId)) {
            throw new IllegalArgumentException(
                    "Submission with ID " + submissionId + " not found"
            );
        }

        // 2. Fetch the feedback record
        Feedback feedback = feedbackRepository.findBySubmissionId(submissionId);
        if (feedback == null) {
            throw new IllegalStateException(
                    "No feedback has been created yet for submission ID " + submissionId
            );
        }

        // 3. Map to dto and return
        FeedbackRequest request = new FeedbackRequest();
        request.setSubmissionID(submissionId);
        request.setComment(feedback.getComments());
        request.setGrade(feedback.getGrade());
        return request;
    }


}
