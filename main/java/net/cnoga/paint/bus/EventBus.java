package net.cnoga.paint.bus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * A simple publish/subscribe event bus.
 * <p>
 * Subscribers register methods to handle events of a specific type, and publishers post events to
 * be delivered to all matching subscribers.
 *
 * @author cnoga
 * @version 1.1
 */
public class EventBus {

  private static final EventBus INSTANCE = new EventBus();

  private final Map<Class<?>, List<Consumer<?>>> listeners = new ConcurrentHashMap<>();

  // private constructor to enforce singleton
  private EventBus() {
  }

  /**
   * Get the global singleton instance of the EventBus.
   */
  public static EventBus getInstance() {
    return INSTANCE;
  }

  /**
   * Register all @SubscribeEvent methods in a class marked @EventBusSubscriber. Methods must take
   * exactly one parameter = event type.
   *
   * @param subscriber - The subscriber to the event bus.
   */
  public void register(Object subscriber) {
    Class<?> clazz = subscriber.getClass();
    System.out.println("[EventBus] Registered: " + clazz);

    // Enforce class level annotations, as it doesn't make
    // sense for a SubscribeEvent method to not be subscribed
    // to the event bus.
    if (!clazz.isAnnotationPresent(EventBusSubscriber.class)) {
      throw new IllegalArgumentException(
        "Class " + clazz.getName() + " must be annotated with @EventBusSubscriber"
      );
    }

    for (Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(SubscribeEvent.class)) {
        // Check # of params
        Class<?>[] params = method.getParameterTypes();
        if (params.length != 1) {
          throw new IllegalArgumentException(
            "@SubscribeEvent method " + method.getName() +
              " must have exactly ONE parameter (the event type)."
          );
        }

        Class<?> eventType = params[0];
        method.setAccessible(true);

        // wrap the reflection's call in a consumer
        Consumer<Object> consumer = event -> {
          try {
            method.invoke(subscriber, event);
          } catch (Exception e) {
            throw new RuntimeException(
              "Failed to invoke subscriber method: " + method, e
            );
          }
        };

        listeners
          .computeIfAbsent(eventType, k -> new ArrayList<>())
          .add(consumer);
      }

    }
  }

  /**
   * Post an event to all subscribers on the event bus listening for its type.
   *
   * @param event - The event the bus posts.
   */
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