package com.app.lms.notification_management.event_bus.events;

import com.app.lms.notification_management.event_bus.Event;

public class FeedbackCreatedEvent extends Event {
    private final Long feedbackId;

    public FeedbackCreatedEvent(Long feedbackId) {
        this.feedbackId = feedbackId;
    }
    public Long getFeedbackId() {
        return this.feedbackId;
    }
}
