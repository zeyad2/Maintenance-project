package com.app.lms.notification_management.event_bus;

public interface event_bus{
    void publish(Event event);
    void register(Class<? extends Event> eventType, EventListener<? extends Event> listener);
}
