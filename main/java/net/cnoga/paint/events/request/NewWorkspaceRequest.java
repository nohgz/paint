package net.cnoga.paint.events.request;

import net.cnoga.paint.brews.WorkspaceBrew;

/**
 * Event signaling that the user has requested creation of a new workspace.
 * <p>
 * Typically posted by the {@code FileIOPublisher} when "New File" is chosen. Listeners should
 * respond by preparing a fresh canvas or workspace.
 * <p>
 * See {@link WorkspaceBrew} for an implementation of a listener implementing this logic.
 *
 * @author cnoga
 * @version 1.0
 */
public record NewWorkspaceRequest(int width, int height) {

}
