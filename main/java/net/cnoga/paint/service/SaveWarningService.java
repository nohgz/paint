package net.cnoga.paint.service;

import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.events.request.FocusWorkspaceRequest;
import net.cnoga.paint.events.request.WorkspaceSaveAsRequest;
import net.cnoga.paint.workspace.Workspace;

@EventBusSubscriber
public class SaveWarningService extends EventBusPublisher  {
  final Stage primaryStage;

  public SaveWarningService(Stage primaryStage) {
    this.primaryStage = primaryStage;
    bus.register(this);
  }

  public void promptWorkspaceClose(Workspace ws, Runnable onClose) {
    if (!ws.getDirtyFlag()) {
      onClose.run();
      return;
    }

    List<Workspace> single = List.of(ws);
    showDialog(single, onClose);
  }

  public void promptProgramClose(List<Workspace> dirtyWorkspaces, Runnable onCloseAll) {
    if (dirtyWorkspaces.isEmpty()) {
      onCloseAll.run();
      return;
    }
    showDialog(dirtyWorkspaces, onCloseAll);
  }

  private void showDialog(List<Workspace> dirtyWorkspaces, Runnable onCloseAction) {
    Stage dialog = new Stage();
    dialog.initOwner(primaryStage);
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.setTitle("Unsaved Changes");

    VBox root = new VBox(10);
    root.setAlignment(Pos.CENTER);
    Label label = new Label(dirtyWorkspaces.size() == 1 ?
      "This workspace has unsaved changes. What do you want to do?" :
      "You have unsaved changes in " + dirtyWorkspaces.size() + " workspaces. What do you want to do?");
    label.setWrapText(true);

    HBox buttons = new HBox(10);
    buttons.setAlignment(Pos.CENTER);

    Button save = new Button("Save");
    Button discard = new Button(dirtyWorkspaces.size() == 1 ? "Discard & Exit" : "Discard All & Exit");
    Button cancel = new Button("Cancel");

    buttons.getChildren().addAll(save, discard, cancel);
    root.getChildren().addAll(label, buttons);

    Scene scene = new Scene(root, 350, 150);
    dialog.setScene(scene);

    save.setOnAction(e -> {
      dirtyWorkspaces.forEach(ws -> {
        // focus then save. kind of how PDN does it
        bus.post(new FocusWorkspaceRequest(ws));
        bus.post(new WorkspaceSaveAsRequest());
      });
      onCloseAction.run();
      dialog.close();
    });

    discard.setOnAction(e -> {
      onCloseAction.run();
      dialog.close();
    });

    cancel.setOnAction(e -> dialog.close());

    dialog.showAndWait();
  }
}
