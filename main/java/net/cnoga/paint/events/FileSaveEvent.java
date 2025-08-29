package net.cnoga.paint.events;

/**
 * Event signaling that the current workspace should be saved to its
 * associated file location.
 * <p>
 * Typically posted by the {@link net.cnoga.paint.publisher.FileIOPublisher} when "New File" is chosen.
 * <p>
 * If no file is currently associated, a {@link FileSaveAsEvent} should be
 * triggered instead.
 * <p>
 * See {@link net.cnoga.paint.listener.WorkspaceListener}
 * for an implementation of a listener implementing this logic.
 *
 * @author cnoga
 * @version 1.0
 */
public record FileSaveEvent() {

}
