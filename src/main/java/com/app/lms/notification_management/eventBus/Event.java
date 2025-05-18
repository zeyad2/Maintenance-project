package com.app.lms.notification_management.eventBus;

public abstract class Event {
    private final long timestamp;

    protected Event() {
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }
}
