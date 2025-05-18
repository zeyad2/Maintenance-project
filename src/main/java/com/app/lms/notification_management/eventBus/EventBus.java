package com.app.lms.notification_management.eventBus;

public interface EventBus {
    void publish(Event event);
    void register(Class<? extends Event> eventType, EventListener<? extends Event> listener);
}
