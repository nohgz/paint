package net.cnoga.paint.core.bus.events.request;

/** Requests to rotate the current selection.
 * @param degrees the rotation angle in degrees
 */
public record RotateSelectionRequest(int degrees) {

}
