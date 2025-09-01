package net.cnoga.paint.events;

import java.io.File;

/**
 * Event signaling that a file has been opened for editing or display.
 * <p>
 * Typically posted by the {@link net.cnoga.paint.publisher.FileIOPublisher} when "Open File" is chosen.
 * Listeners should respond by loading its contents into the active workspace.
 * <p>
 * See {@link net.cnoga.paint.listener.WorkspaceListener}
 * for an implementation of a listener that implements this logic.
 * @param file the file to open and display.
 *
 * @author cnoga
 * @version 1.0
 */
public record FileOpenEvent(File file) {
  // Left empty.
}