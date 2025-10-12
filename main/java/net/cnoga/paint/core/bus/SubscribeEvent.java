package net.cnoga.paint.core.bus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as an event handler for a specific event type.
 * <p>
 * Methods annotated with {@link SubscribeEvent} must have exactly one
 * parameter representing the event type. They will be invoked whenever
 * that event is posted to the {@link EventBus}.
 * </p>
 *
 * <p>Example:</p>
 * <pre>{@code
 * @SubscribeEvent
 * public void onOpen(FileOpenedEvent evt) {
 *     // Do something cool with the file
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubscribeEvent { }
