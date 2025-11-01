package net.cnoga.paint.core.bus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares that a class contains event listener methods for the global event bus.
 * <p>
 * Classes annotated with {@link EventBusSubscriber} can define methods annotated with
 * {@link SubscribeEvent} to receive specific event types.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventBusSubscriber {

}
