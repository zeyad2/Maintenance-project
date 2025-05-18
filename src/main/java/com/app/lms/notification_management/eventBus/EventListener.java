package com.app.lms.notification_management.eventBus;

public interface EventListener<E extends Event> {
    void handle(E event);
}
