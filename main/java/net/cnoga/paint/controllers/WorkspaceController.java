package net.cnoga.paint.controllers;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import net.cnoga.paint.listener.WorkspaceListener;

/**
 * Controller for the main workspace area of the application.
 * <p>
 * Holds references to the workspace's FXML components (scroll pane,
 * stack pane, and canvas group) and provides initialization for
 * the {@link WorkspaceListener}.
 */
public class WorkspaceController {
  public ScrollPane workspaceScrollPane;
  public StackPane workspaceStackPane;
  public Group workspaceCanvasGroup;

  @FXML
  public WorkspaceListener initWorkspaceListener() {
    return new WorkspaceListener(workspaceScrollPane, workspaceStackPane, workspaceCanvasGroup);
  }
}