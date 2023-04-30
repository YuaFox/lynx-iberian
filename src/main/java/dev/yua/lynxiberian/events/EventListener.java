package dev.yua.lynxiberian.events;

public interface EventListener<T extends Event, V> {
    V onEvent(T event);
}
