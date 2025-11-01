package net.cnoga.paint.core.bus.events.response;

import net.cnoga.paint.core.workspace.Workspace;

/** Event fired when a workspace is saved under a new name or location.
 * @param workspace the saved {@link Workspace}
 */
public record WorkspaceSavedAsEvent(Workspace workspace) {

}
