package com.app.lms.notification_management;

import com.app.lms.assessment_management.service.AssignmentService;
import com.app.lms.assessment_management.service.FeedbackService;
import com.app.lms.assessment_management.service.QuizService;
import com.app.lms.course_management.service.CourseService;
import com.app.lms.course_management.service.LessonService;
import com.app.lms.notification_management.eventBus.events.*;
import com.app.lms.notification_management.eventBus.listeners.*;
import com.app.lms.user_management.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import com.app.lms.notification_management.notification.NotificationService;
import com.app.lms.notification_management.eventBus.EventBus;
import com.app.lms.notification_management.eventBus.GeneralEventBus;

@Configuration
public class NotificationManagementConfiguration {

    @Bean
    public EventBus eventBus(NotificationService notificationService, CourseService courseService, UserService userService, LessonService lessonService, QuizService quizService, AssignmentService assignmentService, FeedbackService feedbackService) {
        GeneralEventBus eventBus = new GeneralEventBus();
        eventBus.register(EnrollmentEvent.class, new EnrollmentEventListener(notificationService, courseService, userService));
        eventBus.register(AddedLessonEvent.class, new AddedLessonEventListener(notificationService, courseService));
        eventBus.register(MaterialUploadedEvent.class, new MaterialUploadedEventListener(notificationService, courseService, lessonService));
        eventBus.register(QuizCreatedEvent.class, new QuizCreatedEventListener(quizService, courseService, notificationService));
        eventBus.register(AssignmentCreatedEvent.class, new AssignmentCreatedEventListener(assignmentService, courseService, notificationService));
        eventBus.register(FeedbackCreatedEvent.class, new FeedbackCreatedEventListener(feedbackService, notificationService));
        return eventBus;
    }
}
