package com.app.lms.notification_management.event_bus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Generalevent_bus implements event_bus {
    private final Map<Class<? extends Event>, List<EventListener<? extends Event>>> listeners = new ConcurrentHashMap<>();

    @Override
    public void publish(Event event) {
        List<EventListener<? extends Event>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (EventListener<? extends Event> listener : eventListeners) {
                notifyListener(listener, event);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <E extends Event> void notifyListener(EventListener<? extends Event> listener, Event event) {
        ((EventListener<E>) listener).handle((E) event);
    }

    @Override
    public void register(Class<? extends Event> eventType, EventListener<? extends Event> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }
}
