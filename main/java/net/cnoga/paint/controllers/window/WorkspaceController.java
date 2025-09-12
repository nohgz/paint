package net.cnoga.paint.controllers.window;

import javafx.scene.control.TabPane;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.events.init.InitWorkspaceServiceRequest;
import net.cnoga.paint.brews.WorkspaceBrew;

/**
 * Controller for the main workspace area of the application.
 * <p>
 * Holds references to the workspace's FXML components (scroll pane, stack pane, and canvas group)
 * and provides initialization for the {@link WorkspaceBrew}.
 */
public class WorkspaceController extends EventBusPublisher {

  public TabPane workspaceTabPane;

  public void initWorkspaceService() {
    bus.post(new InitWorkspaceServiceRequest(workspaceTabPane));
  }
}