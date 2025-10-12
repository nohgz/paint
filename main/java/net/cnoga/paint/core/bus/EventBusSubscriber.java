package net.cnoga.paint.core.bus;

import java.lang.annotation.*;

/**
 * Declares that a class contains event listener methods for the global event bus.
 * <p>
 * Classes annotated with {@link EventBusSubscriber} can define methods annotated
 * with {@link SubscribeEvent} to receive specific event types.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventBusSubscriber { }
