package net.cnoga.paint.core.bus;

/**
 * Base class providing access to the shared application {@link EventBus}.
 *
 * <p>This class exposes a protected reference to the global {@link EventBus}
 * instance so that subclasses can publish or subscribe to application-wide events. It is typically
 * extended by services, controllers, or components that need to participate in the event-driven
 * communication layer.</p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * public class MyService extends EventBusPublisher {
 *     public void doSomething() {
 *         bus.post(new MyCustomEvent(...));
 *     }
 * }
 * }</pre>
 *
 * @author cnoga
 * @version 1.0
 */
public class EventBusPublisher {

  protected final EventBus bus = EventBus.getInstance();
}
