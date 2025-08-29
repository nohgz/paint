package net.cnoga.paint.events;

/**
 * Event signaling that the user has requested creation of a new workspace.
 * <p>
 * Typically posted by the {@code FileIOPublisher} when "New File" is chosen.
 * Listeners should respond by preparing a fresh canvas or workspace.
 * <p>
 * See {@link net.cnoga.paint.listener.WorkspaceListener}
 * for an implementation of a listener implementing this logic.
 *
 * @author cnoga
 * @version 1.0
 */
public record NewFileEvent() {
  // Left empty. The canvas service is to intercept and actually show the new image.
}
