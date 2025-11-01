package net.cnoga.paint.core.bus.events.request;

import net.cnoga.paint.core.brews.WorkspaceBrew;

/**
 * Event signaling that the current workspace should be saved to its associated file location.
 * <p>
 * If no file is currently associated, a {@link WorkspaceSaveAsRequest} should be triggered
 * instead.
 * <p>
 * See {@link WorkspaceBrew} for an implementation of a listener implementing this logic.
 */
public record WorkspaceSaveRequest() {

}
