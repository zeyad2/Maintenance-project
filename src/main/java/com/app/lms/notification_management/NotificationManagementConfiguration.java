package com.app.lms.notification_management;

import com.app.lms.assessment_management.service.AssignmentService;
import com.app.lms.assessment_management.service.FeedbackService;
import com.app.lms.assessment_management.service.QuizService;
import com.app.lms.course_management.service.CourseService;
import com.app.lms.course_management.service.LessonService;
import com.app.lms.notification_management.event_bus.events.*;
import com.app.lms.notification_management.event_bus.listeners.*;
import com.app.lms.user_management.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import com.app.lms.notification_management.notification.NotificationService;
import com.app.lms.notification_management.event_bus.event_bus;
import com.app.lms.notification_management.event_bus.Generalevent_bus;

@Configuration
public class NotificationManagementConfiguration {

    @Bean
    public event_bus event_bus(NotificationService notificationService, CourseService courseService, UserService userService, LessonService lessonService, QuizService quizService, AssignmentService assignmentService, FeedbackService feedbackService) {
        Generalevent_bus event_bus = new Generalevent_bus();
        event_bus.register(EnrollmentEvent.class, new EnrollmentEventListener(notificationService, courseService, userService));
        event_bus.register(AddedLessonEvent.class, new AddedLessonEventListener(notificationService, courseService));
        event_bus.register(MaterialUploadedEvent.class, new MaterialUploadedEventListener(notificationService, courseService, lessonService));
        event_bus.register(QuizCreatedEvent.class, new QuizCreatedEventListener(quizService, courseService, notificationService));
        event_bus.register(AssignmentCreatedEvent.class, new AssignmentCreatedEventListener(assignmentService, courseService, notificationService));
        event_bus.register(FeedbackCreatedEvent.class, new FeedbackCreatedEventListener(feedbackService, notificationService));
        return event_bus;
    }
}
