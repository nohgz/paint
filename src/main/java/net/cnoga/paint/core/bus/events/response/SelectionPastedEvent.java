package net.cnoga.paint.core.bus.events.response;

/** Event fired when a selection is pasted.
 * @param x the x-coordinate of the paste location
 * @param y the y-coordinate of the paste location
 */
public record SelectionPastedEvent(double x, double y) {

}
