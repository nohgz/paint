package net.cnoga.paint.core.bus.events.request;

import net.cnoga.paint.core.brews.FileIOBrew;
import net.cnoga.paint.core.brews.WorkspaceBrew;

/**
 * Event signaling that the current workspace should be saved to its associated file location.
 * <p>
 * Typically posted by the {@link FileIOBrew} when "Save" is chosen.
 * <p>
 * If no file is currently associated, a {@link WorkspaceSaveAsRequest} should be triggered
 * instead.
 * <p>
 * See {@link WorkspaceBrew} for an implementation of a listener implementing this logic.
 *
 * @author cnoga
 * @version 1.0
 */
public record WorkspaceSaveRequest() {

}
