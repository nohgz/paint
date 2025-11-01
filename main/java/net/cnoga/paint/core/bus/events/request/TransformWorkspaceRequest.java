package net.cnoga.paint.core.bus.events.request;

/** Requests a whole workspace transformation.
 * @param degrees the rotation angle in degrees
 * @param mirrorX whether to mirror horizontally
 * @param mirrorY whether to mirror vertically
 */
public record TransformWorkspaceRequest(int degrees, boolean mirrorX, boolean mirrorY) {

}
