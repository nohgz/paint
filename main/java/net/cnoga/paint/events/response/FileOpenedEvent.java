package net.cnoga.paint.events.response;

import java.io.File;
import net.cnoga.paint.brews.FileIOBrew;
import net.cnoga.paint.brews.WorkspaceBrew;

/**
 * Event signaling that a file has been opened for editing or display.
 * <p>
 * Typically posted by the {@link FileIOBrew} when "Open File" is chosen. Listeners should
 * respond by loading its contents into the active workspace.
 * <p>
 * See {@link WorkspaceBrew} for an implementation of a listener that implements this logic.
 *
 * @param file the file to open and display.
 * @author cnoga
 * @version 1.0
 */
public record FileOpenedEvent(File file) {
  // Left empty.
}