package net.cnoga.paint.core.bus.events.request;

import net.cnoga.paint.core.workspace.Workspace;

/** Requests focus for the specified workspace.
 * @param workspace the workspace to focus
 */
public record FocusWorkspaceRequest(Workspace workspace) {

}
