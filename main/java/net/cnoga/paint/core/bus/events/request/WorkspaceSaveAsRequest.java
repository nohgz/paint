package net.cnoga.paint.core.bus.events.request;

import net.cnoga.paint.core.brews.WorkspaceBrew;

/**
 * Event signaling that the current workspace should be saved under a file name specified by the
 * user.
 * <p>
 * Typically posted by Controllers when "Save As" is chosen. Listeners should respond by showing a
 * "Save As" dialog and then writing the canvas contents to the selected file.
 * <p>
 * See {@link WorkspaceBrew} for an implementation of a listener that implements this logic.
 *
 *
 *
 */
public record WorkspaceSaveAsRequest() {
  // Left empty, as the actual saving of the image is left to the CanvasService.
}
