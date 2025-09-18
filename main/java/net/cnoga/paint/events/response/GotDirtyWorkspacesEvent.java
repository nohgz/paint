package net.cnoga.paint.events.response;

import java.util.List;
import net.cnoga.paint.workspace.Workspace;

public record GotDirtyWorkspacesEvent(List<Workspace> dirtyWorkspaces) {

}
