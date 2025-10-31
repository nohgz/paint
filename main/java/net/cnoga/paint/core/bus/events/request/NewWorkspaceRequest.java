package net.cnoga.paint.core.bus.events.request;

import net.cnoga.paint.core.brews.WorkspaceBrew;

/**
 * Event signaling that the user has requested creation of a new workspace.
 * <p>
 * Typically posted by the {@code FileIOPublisher} when "New File" is chosen. Listeners should
 * respond by preparing a fresh canvas or workspace.
 * <p>
 * See {@link WorkspaceBrew} for an implementation of a listener implementing this logic.
 *
 *
 *
 */
public record NewWorkspaceRequest(int width, int height) {

}
