package net.cnoga.paint.core.bus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * A simple event bus for program-wide reactive programming tasks.
 * <p>This implementation uses reflection to discover subscriber methods
 * annotated with {@link SubscribeEvent} in classes annotated with {@link EventBusSubscriber}.
 * </p>
 *
 * <p>
 * Subscribers register listener methods for specific event types, and publishers post events that
 * are automatically delivered to all matching subscribers.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * @EventBusSubscriber
 * public class ToolManager {
 *     @SubscribeEvent
 *     public void onUndo(UndoRequestEvent e) {
 *         // handle undo
 *     }
 * }
 * }</pre>
 *
 * @author cnoga
 * @version 1.2
 */
public class EventBus {

  /**
   * Singleton instance of the global event bus.
   */
  private static final EventBus INSTANCE = new EventBus();

  /**
   * Map of event type to subscribers interested in that type.
   */
  private final Map<Class<?>, List<Consumer<?>>> listeners = new ConcurrentHashMap<>();

  /**
   * Private constructor to enforce singleton use.
   */
  private EventBus() {
  }

  /**
   * Returns the shared singleton {@link EventBus} instance. Only the launcher should be aware of
   * this.
   *
   * @return global event bus instance
   */
  public static EventBus getInstance() {
    return INSTANCE;
  }

  /**
   * Registers all {@link SubscribeEvent} methods of an {@link EventBusSubscriber}.
   * <p>
   * Each annotated method must take exactly one argument.
   * </p>
   *
   * @param subscriber object containing event handler methods.
   * @throws IllegalArgumentException if the class or methods are misannotated.
   */
  public void register(Object subscriber) {
    Class<?> clazz = subscriber.getClass();
    System.out.println("[EventBus] Registered: " + clazz);

    if (!clazz.isAnnotationPresent(EventBusSubscriber.class)) {
      throw new IllegalArgumentException(
        "Class " + clazz.getName() + " must be annotated with @EventBusSubscriber"
      );
    }

    for (Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(SubscribeEvent.class)) {
        Class<?>[] params = method.getParameterTypes();
        if (params.length != 1) {
          throw new IllegalArgumentException(
            "@SubscribeEvent method " + method.getName() +
              " must have exactly ONE parameter (the event type)."
          );
        }

        Class<?> eventType = params[0];
        method.setAccessible(true);

        Consumer<Object> consumer = event -> {
          try {
            method.invoke(subscriber, event);
          } catch (Exception e) {
            throw new RuntimeException(
              "Failed to invoke subscriber method: " + method, e
            );
          }
        };

        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(consumer);
      }
    }
  }

  /**
   * Publishes an event to all listeners registered for its type.
   *
   * @param event the event to post
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
