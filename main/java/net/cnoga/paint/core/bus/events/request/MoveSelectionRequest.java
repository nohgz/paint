package net.cnoga.paint.core.bus.events.request;

/** Requests to move the current selection by the given offset.
 * @param dx the horizontal offset
 * @param dy the vertical offset
 * <p>
 * Note that dy is inverted.
 */
public record MoveSelectionRequest(double dx, double dy) {

}
