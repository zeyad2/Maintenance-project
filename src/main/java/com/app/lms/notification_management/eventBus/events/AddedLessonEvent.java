package com.app.lms.notification_management.eventBus.events;

import com.app.lms.notification_management.eventBus.Event;

public class AddedLessonEvent extends Event {
    private final Long courseId;

    public AddedLessonEvent(Long courseId) {
        this.courseId = courseId;
    }

    public Long getCourseId() {
        return this.courseId;
    }

}
