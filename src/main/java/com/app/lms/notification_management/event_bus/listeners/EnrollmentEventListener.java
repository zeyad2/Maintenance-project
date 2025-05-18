package com.app.lms.notification_management.event_bus.listeners;

import com.app.lms.course_management.model.Course;
import com.app.lms.course_management.service.CourseService;
import com.app.lms.notification_management.event_bus.EventListener;
import com.app.lms.notification_management.event_bus.events.EnrollmentEvent;
import com.app.lms.notification_management.notification.NotificationService;
import com.app.lms.user_management.model.User;
import com.app.lms.user_management.service.UserService;

public class EnrollmentEventListener implements EventListener<EnrollmentEvent> {

    private final NotificationService notificationService;
    private final CourseService courseService;
    private final UserService userService;

    public EnrollmentEventListener(NotificationService notificationService, CourseService courseService,
            UserService userService) {
        this.notificationService = notificationService;
        this.courseService = courseService;
        this.userService = userService;
    }

    @Override
    public void handle(EnrollmentEvent event) {
        Long studentId = event.getStudentId();
        Long courseId = event.getCourseId();

        User student = userService.findById(studentId).orElse(null);
        Course course = courseService.findCourseById(courseId);
        User instructor = course.getInstructor();

        String studentMessage = "You have successfully enrolled in course: " + course.getTitle();
        String instructorMessage = "A new student has enrolled in your course: " + course.getTitle() + "\n Student ID: "
                + studentId;

        // Send in-app notifications
        notificationService.createNotification(studentId, studentMessage);
        notificationService.createNotification(instructor.getId(), instructorMessage);

        // Send mail notifications, but only if student is not null
        if (student != null) {
            notificationService.sendEmailNotification(student.getEmail(), "Successful Enrollment", studentMessage);
        } else {
            // Handle the case when student is null, e.g., log a warning
            System.err.println("Warning: Student with ID " + studentId + " not found. Email not sent.");
        }
        notificationService.sendEmailNotification(instructor.getEmail(), "Student Enrolled In Your Course",
                instructorMessage);
    }

}
