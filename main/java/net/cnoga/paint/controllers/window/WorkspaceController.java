package net.cnoga.paint.controllers.window;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.events.request.InitWorkspaceRequest;
import net.cnoga.paint.service.WorkspaceService;

/**
 * Controller for the main workspace area of the application.
 * <p>
 * Holds references to the workspace's FXML components (scroll pane,
 * stack pane, and canvas group) and provides initialization for
 * the {@link WorkspaceService}.
 */
public class WorkspaceController extends EventBusPublisher {
  public ScrollPane workspaceScrollPane;
  public StackPane workspaceStackPane;
  public Group workspaceCanvasGroup;
  public void initWorkspaceService() {
    bus.post(new InitWorkspaceRequest(workspaceScrollPane, workspaceStackPane, workspaceCanvasGroup));
  }
}