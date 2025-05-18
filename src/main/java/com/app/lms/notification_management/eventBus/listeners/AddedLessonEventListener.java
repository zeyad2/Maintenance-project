package com.app.lms.notification_management.eventBus.listeners;

import com.app.lms.DTO.StudentInfoDTO;
import com.app.lms.notification_management.eventBus.EventListener;
import com.app.lms.notification_management.eventBus.events.AddedLessonEvent;
import com.app.lms.notification_management.notification.NotificationService;
import com.app.lms.course_management.service.CourseService;

import java.util.List;

public class AddedLessonEventListener implements EventListener<AddedLessonEvent> {
    private final NotificationService notificationService;
    private final CourseService courseService;

    public AddedLessonEventListener(NotificationService notificationService, CourseService courseService) {
        this.notificationService = notificationService;
        this.courseService = courseService;
    }

    @Override
    public void handle(AddedLessonEvent event){
        Long courseId = event.getCourseId();
        List<StudentInfoDTO> enrolledStudents = courseService.getEnrolledStudents(courseId);

        String message = "New lesson was added in course " + courseId;

        // Send in app notifications
        for(StudentInfoDTO student : enrolledStudents) {
            notificationService.createNotification(student.getStudentId(), message);
        }
        // Send mail notifications
        for(StudentInfoDTO student : enrolledStudents){
            notificationService.sendEmailNotification(student.getEmail(), "New Lesson Added", message);
        }
    }
}
