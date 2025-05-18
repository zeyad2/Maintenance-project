package com.app.lms.notification_management.event_bus.events;

import com.app.lms.notification_management.event_bus.Event;

public class QuizCreatedEvent extends Event {
    private final Long quizId;
    public QuizCreatedEvent(Long quizId) {
        this.quizId = quizId;
    }
    public Long getQuizId() {
        return this.quizId;
    }
}
