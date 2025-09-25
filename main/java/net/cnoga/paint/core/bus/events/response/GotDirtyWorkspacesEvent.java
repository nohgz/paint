package net.cnoga.paint.core.bus.events.response;

import java.util.List;
import net.cnoga.paint.core.workspace.Workspace;

public record GotDirtyWorkspacesEvent(List<Workspace> dirtyWorkspaces) {

}
