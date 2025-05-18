package com.app.lms.notification_management.eventBus.listeners;

import com.app.lms.DTO.StudentInfoDTO;
import com.app.lms.course_management.model.Lesson;
import com.app.lms.course_management.service.CourseService;
import com.app.lms.course_management.service.LessonService;
import com.app.lms.notification_management.eventBus.EventListener;
import com.app.lms.notification_management.eventBus.events.MaterialUploadedEvent;
import com.app.lms.notification_management.notification.NotificationService;

import java.util.List;

public class MaterialUploadedEventListener implements EventListener<MaterialUploadedEvent> {
    private final NotificationService notificationService;
    private final CourseService courseService;
    private final LessonService lessonService;

    public MaterialUploadedEventListener(NotificationService notificationService, CourseService courseService, LessonService lessonService) {
        this.notificationService = notificationService;
        this.courseService = courseService;
        this.lessonService = lessonService;

    }

    @Override
    public void handle(MaterialUploadedEvent event) {
        Long lessonId = event.getLessonId();
        Lesson lesson = lessonService.getByID(lessonId);
        Long courseId = lesson.getCourse().getId();

        List<StudentInfoDTO> enrolledStudents = courseService.getEnrolledStudents(courseId);

        String message = "New material is added to lesson: " + lesson.getTitle() + " In " + lesson.getCourse().getTitle() + " Course";

        // Send in app notifications
        for (StudentInfoDTO student : enrolledStudents) {
            notificationService.createNotification(student.getStudentId(), message);
        }
        // Send mail notifications
        for (StudentInfoDTO student : enrolledStudents) {
            notificationService.sendEmailNotification(student.getEmail(), "New Material Added", message);
        }
    }
}
