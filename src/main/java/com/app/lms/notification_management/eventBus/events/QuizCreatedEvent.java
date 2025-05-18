package com.app.lms.notification_management.eventBus.events;

import com.app.lms.notification_management.eventBus.Event;

public class QuizCreatedEvent extends Event {
    private final Long quizId;
    public QuizCreatedEvent(Long quizId) {
        this.quizId = quizId;
    }
    public Long getQuizId() {
        return this.quizId;
    }
}
