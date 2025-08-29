package net.cnoga.paint.bus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a subscriber to the global event bus.
 * <p>
 * Classes annotated with {@code @EventBusSubscriber} are discovered and
 * automatically registered so their methods annotated with
 * {@link SubscribeEvent} can receive events.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventBusSubscriber { }