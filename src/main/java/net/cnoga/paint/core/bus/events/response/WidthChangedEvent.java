package net.cnoga.paint.core.bus.events.response;

/** Event indicating the global stroke width has changed.
 * @param width the new stroke width in pixels
 */
public record WidthChangedEvent(Integer width) {

}
