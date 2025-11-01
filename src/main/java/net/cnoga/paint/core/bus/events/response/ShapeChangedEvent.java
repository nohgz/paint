package net.cnoga.paint.core.bus.events.response;

import net.cnoga.paint.core.util.ShapeConfig;

/** Event indicating the active shape configuration has changed.
 * @param shapeConfig the new {@link ShapeConfig}
 */
public record ShapeChangedEvent(ShapeConfig shapeConfig) {

}
