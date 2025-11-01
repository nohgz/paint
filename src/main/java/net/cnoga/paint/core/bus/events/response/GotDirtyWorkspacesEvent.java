package net.cnoga.paint.core.bus.events.response;

import java.util.List;
import net.cnoga.paint.core.workspace.Workspace;

/** Event containing the list of unsaved (dirty) workspaces.
 * @param dirtyWorkspaces the list of dirty {@link Workspace} instances
 */
public record GotDirtyWorkspacesEvent(List<Workspace> dirtyWorkspaces) {

}
