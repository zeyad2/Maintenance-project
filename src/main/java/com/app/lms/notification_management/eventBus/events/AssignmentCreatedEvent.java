package com.app.lms.notification_management.eventBus.events;

import com.app.lms.notification_management.eventBus.Event;

public class AssignmentCreatedEvent extends Event {
    private final Long assignmentId;

    public AssignmentCreatedEvent(Long assignmentId){
        this.assignmentId = assignmentId;
    }

    public Long getAssignmentId() {
        return this.assignmentId;
    }
}
