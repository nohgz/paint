package net.cnoga.paint.core.bus.events.response;

import net.cnoga.paint.core.workspace.Workspace;

/** Event fired when a workspace is successfully saved.
 * @param workspace the saved {@link Workspace}
 */
public record WorkspaceSavedEvent(Workspace workspace) {

}
