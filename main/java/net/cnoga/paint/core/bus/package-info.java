/**
 * Provides a lightweight, annotation-driven event bus for decoupled communication between
 * components of the paint application.
 *
 * <h2>Usage</h2>
 * <p>To define an event listener:</p>
 * <pre>{@code
 * @EventBusSubscriber
 * public class ToolManager extends EventBusPublisher {
 *     public ToolManager() {
 *       bus.register(this); // or register the class when it's created in another way. this is the simplest.
 *     }
 *
 *     @SubscribeEvent
 *     public void onUndo(UndoRequestEvent e) {
 *         // handle undo request
 *     }
 * }
 * }</pre>
 *
 * <p>To publish an event:</p>
 * 1. Extend the {@link net.cnoga.paint.core.bus.EventBusPublisher} class. 2. Register your subclass
 * to the bus
 * <pre>{@code
 * EventBus.getInstance().post(new UndoRequestEvent());
 * }</pre>
 *
 * <p>All dispatch is synchronous by default; if used from background threads,
 * callers should ensure UI-bound code executes on the JavaFX Application Thread.</p>
 */
package net.cnoga.paint.core.bus;