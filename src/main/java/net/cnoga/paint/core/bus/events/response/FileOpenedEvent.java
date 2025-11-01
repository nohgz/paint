package net.cnoga.paint.core.bus.events.response;

import java.io.File;
import net.cnoga.paint.core.brews.WorkspaceBrew;

/**
 * Event signaling that a file has been opened for editing or display.
 * <p>
 * See {@link WorkspaceBrew} for an implementation of a listener that implements this logic.
 *
 * @param file the file to open and display.
 */
public record FileOpenedEvent(File file) {
  // Left empty.
}