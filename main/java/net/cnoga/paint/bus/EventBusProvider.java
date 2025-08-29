package net.cnoga.paint.bus;

/**
 * Base class for any component that owns and exposes an {@link EventBus}.
 * <p>
 * Subclasses typically use the protected {@code bus} to publish or dispatch
 * events, while other objects may register as subscribers to receive them.
 * This class provides a common contract for working with the event bus
 * without dictating how events are actually produced.
 */
public abstract class EventBusProvider {
  protected final EventBus bus;
  public EventBusProvider(EventBus bus) {
    this.bus = bus;
  }
}
