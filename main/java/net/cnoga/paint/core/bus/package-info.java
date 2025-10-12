/**
 * Provides a lightweight, annotation-driven event bus for decoupled communication
 * between components of the paint application.
 *
 * <p>This package implements a simple publish/subscribe mechanism where event
 * producers post arbitrary event objects and listeners automatically receive
 * them if they have subscribed via annotated methods.</p>
 *
 * <h2>Usage</h2>
 * <p>To define an event listener:</p>
 * <pre>{@code
 * @EventBusSubscriber
 * public class ToolManager {
 *     @SubscribeEvent
 *     public void onUndo(UndoRequestEvent e) {
 *         // handle undo request
 *     }
 * }
 * }</pre>
 *
 * <p>To publish an event:</p>
 * 1. Extend the {@link net.cnoga.paint.core.bus.EventBusPublisher} class.
 * 2. Register your subclass to the bus
 * <pre>{@code
 * EventBus.getInstance().post(new UndoRequestEvent());
 * }</pre>
 *
 * <h2>Design</h2>
 * <ul>
 *   <li>{@link net.cnoga.paint.core.bus.EventBus} — central singleton managing subscriptions and event dispatch.</li>
 *   <li>{@link net.cnoga.paint.core.bus.EventBusSubscriber} — class-level marker for subscriber discovery.</li>
 *   <li>{@link net.cnoga.paint.core.bus.SubscribeEvent} — method-level marker for event handlers.</li>
 *   <li>{@link net.cnoga.paint.core.bus.EventBusPublisher} — convenience superclass exposing the shared bus reference.</li>
 * </ul>
 *
 * <p>All dispatch is synchronous by default; if used from background threads,
 * callers should ensure UI-bound code executes on the JavaFX Application Thread.</p>
 */
package net.cnoga.paint.core.bus;