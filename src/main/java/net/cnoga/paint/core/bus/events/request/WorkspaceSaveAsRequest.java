package net.cnoga.paint.core.bus.events.request;

import net.cnoga.paint.core.brews.WorkspaceBrew;

/**
 * Event signaling that the current workspace should be saved under a file name specified by the
 * user.
 * <p>
 * See {@link WorkspaceBrew} for an implementation of a listener that implements this logic.
 */
public record WorkspaceSaveAsRequest() {
  // Left empty, as the actual saving of the image is left to the CanvasService.
}
