package com.app.lms.notification_management.eventBus.events;

import com.app.lms.notification_management.eventBus.Event;

public class MaterialUploadedEvent extends Event {
    private final Long lessonId;

    public MaterialUploadedEvent(Long lessonId) {
        this.lessonId = lessonId;
    }

    public Long getLessonId() {
        return this.lessonId;
    }

}
