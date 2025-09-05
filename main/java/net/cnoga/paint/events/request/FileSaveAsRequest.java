package net.cnoga.paint.events.request;

import net.cnoga.paint.service.WorkspaceService;

/**
 * Event signaling that the current workspace should be saved under a
 * file name specified by the user.
 * <p>
 * Typically posted by Controllers when "Save As" is chosen.
 * Listeners should respond by showing a "Save As" dialog and then writing the canvas contents
 * to the selected file.
 * <p>
 * See {@link WorkspaceService} for an implementation of a listener that implements this logic.
 *
 * @author cnoga
 * @version 1.0
 */
public record FileSaveAsRequest() {
  // Left empty, as the actual saving of the image is left to the CanvasService.
}
