package net.cnoga.paint.bus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class EventBus {

  private final Map<Class<?>, List<Consumer<?>>> listeners = new ConcurrentHashMap<>();

  public <T> void subscribe(Class<T> eventType, Consumer<T> listener) {
    listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
  }

  @SuppressWarnings("unchecked")
  public <T> void post(T event) {
    Class<?> eventType = event.getClass();
    if (listeners.containsKey(eventType)) {
      for (Consumer<?> listener : listeners.get(eventType)) {
        ((Consumer<T>) listener).accept(event);
      }
    }
  }
}
