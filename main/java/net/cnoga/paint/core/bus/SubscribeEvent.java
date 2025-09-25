package net.cnoga.paint.core.bus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as a handler for a specific event type.
 * <p>
 * Methods annotated with {@code @SubscribeEvent} are invoked by the event bus when an event of the
 * corresponding parameter type is posted.
 * <p>
 * Example:
 * <pre>{@code
 * @SubscribeEvent
 * public void onSave(FileSaveEvent event) {
 *  // do stuff
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@SuppressWarnings("unused")
public @interface SubscribeEvent {

}
