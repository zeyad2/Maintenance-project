package com.app.lms.notification_management.eventBus.listeners;

import com.app.lms.DTO.StudentInfoDTO;
import com.app.lms.assessment_management.model.Quiz;
import com.app.lms.assessment_management.service.QuizService;
import com.app.lms.course_management.service.CourseService;
import com.app.lms.notification_management.eventBus.EventListener;
import com.app.lms.notification_management.eventBus.events.QuizCreatedEvent;
import com.app.lms.notification_management.notification.NotificationService;
import java.util.List;

public class QuizCreatedEventListener implements EventListener<QuizCreatedEvent>{
    private final QuizService quizService;
    private final CourseService courseService;
    private final NotificationService notificationService;
    public QuizCreatedEventListener(QuizService quizService, CourseService courseService, NotificationService notificationService) {
        this.quizService = quizService;
        this.courseService = courseService;
        this.notificationService = notificationService;
    }

    @Override
    public void handle(QuizCreatedEvent event) {
        Long quizId = event.getQuizId();
        Quiz quiz = quizService.getById(quizId);

        List<StudentInfoDTO> enrolledStudents = courseService.getEnrolledStudents(quiz.getCourse().getId());

        String message = "A new Quiz for " + quiz.getCourse().getTitle() + " course is scheduled at " + quiz.getStartDate();
        // Send in app notifications
        for(StudentInfoDTO student : enrolledStudents) {
            notificationService.createNotification(student.getStudentId(), message);
        }
        // Send mail notifications
        for(StudentInfoDTO student : enrolledStudents){
            notificationService.sendEmailNotification(student.getEmail(), "New Quiz Scheduled", message);
        }
    }
}