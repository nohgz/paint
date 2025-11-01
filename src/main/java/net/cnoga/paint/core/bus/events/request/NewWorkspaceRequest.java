package net.cnoga.paint.core.bus.events.request;

import net.cnoga.paint.core.brews.WorkspaceBrew;

/**
 * Event signaling that the user has requested creation of a new workspace.
 * <p>
 * See {@link WorkspaceBrew} for an implementation of a listener implementing this logic.
 *
 * @param height Height of new workspace
 * @param width Width of new workspace
 */
public record NewWorkspaceRequest(int width, int height) {

}
