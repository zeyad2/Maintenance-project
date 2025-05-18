package com.app.lms.notification_management.event_bus.listeners;

import com.app.lms.dto.StudentInfoDTO;
import com.app.lms.assessment_management.model.Assignment;
import com.app.lms.assessment_management.service.AssignmentService;
import com.app.lms.course_management.service.CourseService;
import com.app.lms.notification_management.event_bus.EventListener;
import com.app.lms.notification_management.event_bus.events.AssignmentCreatedEvent;
import com.app.lms.notification_management.notification.NotificationService;
import java.util.List;

public class AssignmentCreatedEventListener implements EventListener<AssignmentCreatedEvent> {
    private final AssignmentService assignmentService;
    private final CourseService courseService;
    private final NotificationService notificationService;

    public AssignmentCreatedEventListener(AssignmentService assignmentService, CourseService courseService, NotificationService notificationService) {
        this.assignmentService = assignmentService;
        this.courseService = courseService;
        this.notificationService = notificationService;
    }

    @Override
    public void handle(AssignmentCreatedEvent event) {
        Long assignmentId = event.getAssignmentId();
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        List<StudentInfoDTO> enrolledStudents = courseService.getEnrolledStudents(assignment.getCourse().getId());

        String message = "A new assignment is uploaded for " + assignment.getCourse().getTitle() + " Course";
        // Send in app notifications
        for (StudentInfoDTO student : enrolledStudents) {
            notificationService.createNotification(student.getStudentId(), message);
        }
        // Send mail notifications
        for (StudentInfoDTO student : enrolledStudents) {
            notificationService.sendEmailNotification(student.getEmail(), "New Assignment", message);
        }
    }
}
