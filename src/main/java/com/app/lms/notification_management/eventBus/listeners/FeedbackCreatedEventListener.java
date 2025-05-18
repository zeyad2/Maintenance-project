package com.app.lms.notification_management.eventBus.listeners;

import com.app.lms.assessment_management.model.Assignment;
import com.app.lms.assessment_management.model.Feedback;
import com.app.lms.assessment_management.service.FeedbackService;
import com.app.lms.notification_management.eventBus.EventListener;
import com.app.lms.notification_management.eventBus.events.FeedbackCreatedEvent;
import com.app.lms.notification_management.notification.NotificationService;
import com.app.lms.user_management.model.User;

public class FeedbackCreatedEventListener implements EventListener<FeedbackCreatedEvent> {
    private final FeedbackService feedbackService;
    private final NotificationService notificationService;

    public FeedbackCreatedEventListener(FeedbackService feedbackService, NotificationService notificationService) {
        this.feedbackService = feedbackService;
        this.notificationService = notificationService;
    }

    @Override
    public void handle(FeedbackCreatedEvent event) {
        Long feedbackId = event.getFeedbackId();
        Feedback feedback = feedbackService.getFeedbackById(feedbackId);
        User student = feedback.getSubmission().getStudent();
        Assignment assignment = feedback.getSubmission().getAssignment();

        String message = "Your assignment in " + assignment.getCourse().getTitle() + " was graded";

        // Send in app notifications
        notificationService.createNotification(student.getId(), message);
        // Send mail notifications
        notificationService.sendEmailNotification(student.getEmail(), "Assignment Graded", message);
    }
}
