package net.cnoga.paint.core.bus;

/**
 * Convenience base class providing access to the global {@link EventBus}.
 * <p>
 * Inheritors can post or listen to events without needing to retrieve the bus.
 * </p>
 * <p>
 * Typical subclasses include controllers or services.
 * </p>
 */
public class EventBusPublisher {

  /**
   * Shared event bus reference available to subclasses.
   */
  protected final EventBus bus = EventBus.getInstance();
}
