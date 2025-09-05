package net.cnoga.paint.events.request;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;

public record InitWorkspaceRequest(ScrollPane workspaceScrollPane, StackPane workspaceStackPane, Group workspaceCanvasGroup) {

}
