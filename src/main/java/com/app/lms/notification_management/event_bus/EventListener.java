package com.app.lms.notification_management.event_bus;

public interface EventListener<E extends Event> {
    void handle(E event);
}
