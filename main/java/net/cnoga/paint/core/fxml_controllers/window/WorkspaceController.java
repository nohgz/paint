package net.cnoga.paint.core.fxml_controllers.window;

import javafx.scene.control.TabPane;
import net.cnoga.paint.core.brews.WorkspaceBrew;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.events.init.InitWorkspaceBrewRequest;

/**
 * Controller for the main workspace area of the application.
 * <p>
 * Holds references to the workspace's FXML components (scroll pane, stack pane, and canvas group)
 * and provides initialization for the {@link WorkspaceBrew}.
 */
public class WorkspaceController extends EventBusPublisher {

  public TabPane workspaceTabPane;

  public void initWorkspaceService() {
    bus.post(new InitWorkspaceBrewRequest(workspaceTabPane));
  }
}