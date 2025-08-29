package net.cnoga.paint.controllers;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import net.cnoga.paint.listener.WorkspaceListener;

public class WorkspaceController {
  public ScrollPane workspaceScrollPane;
  public StackPane workspaceStackPane;
  public Group workspaceCanvasGroup;

  @FXML
  public WorkspaceListener initWorkspaceListener() {
    return new WorkspaceListener(workspaceScrollPane, workspaceStackPane, workspaceCanvasGroup);
  }
}