package net.cnoga.paint.events;

/**
 * Event signaling that the current workspace should be saved under a
 * file name specified by the user.
 * <p>
 * Typically posted by the {@link net.cnoga.paint.publisher.FileIOPublisher} when "Save As" is chosen.
 * Listeners should respond by showing a "Save As" dialog and then writing the canvas contents
 * to the selected file.
 * <p>
 * See {@link net.cnoga.paint.listener.WorkspaceListener}
 * for an implementation of a listener that implements this logic.
 *
 * @author cnoga
 * @version 1.0
 */
public record FileSaveAsEvent() {
  // Left empty, as the actual saving of the image is left to the CanvasService.
}
