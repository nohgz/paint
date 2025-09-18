package net.cnoga.paint.brews;

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
import net.cnoga.paint.events.request.WorkspaceSaveRequest;
import net.cnoga.paint.workspace.Workspace;

@EventBusSubscriber
public class SaveWarningBrew extends EventBusPublisher {

  public SaveWarningBrew() {
    bus.register(this);
  }

  /**
   * Prompts the user before closing a single workspace with unsaved changes.
   *
   * @param ws      the workspace to check
   * @param onClose action to run if the workspace can be closed
   */
  public void promptWorkspaceClose(Workspace ws, Runnable onClose) {
    if (!ws.isDirty()) {
      onClose.run();
      return;
    }
    showDialog(List.of(ws), onClose);
  }

  /**
   * Prompts the user before closing the entire program if there are dirty workspaces.
   *
   * @param dirtyWorkspaces the list of unsaved workspaces
   * @param onCloseAll      action to run if all can be closed
   */
  public void promptProgramClose(List<Workspace> dirtyWorkspaces, Runnable onCloseAll) {
    if (dirtyWorkspaces.isEmpty()) {
      onCloseAll.run();
      return;
    }
    showDialog(dirtyWorkspaces, onCloseAll);
  }

  /**
   * Displays a modal dialog warning about unsaved changes.
   *
   * <p><b>NOTE:</b> This dialog is currently built directly in code and should be
   * replaced by an FXML-driven implementation. (NYI: extract to FXML + controller.)</p>
   */
  private void showDialog(List<Workspace> dirtyWorkspaces, Runnable onCloseAction) {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.setTitle("Unsaved Changes");

    // --- NYI: replace with FXML layout ---
    VBox root = new VBox(10);
    root.setAlignment(Pos.CENTER);

    Label label = new Label(dirtyWorkspaces.size() == 1
      ? "This workspace has unsaved changes. What do you want to do?"
      : "You have unsaved changes in " + dirtyWorkspaces.size()
        + " workspaces. What do you want to do?");
    label.setWrapText(true);

    HBox buttons = new HBox(10);
    buttons.setAlignment(Pos.CENTER);

    Button save = new Button("Save");
    Button discard = new Button(
      dirtyWorkspaces.size() == 1 ? "Discard & Exit" : "Discard All & Exit");
    Button cancel = new Button("Cancel");

    buttons.getChildren().addAll(save, discard, cancel);
    root.getChildren().addAll(label, buttons);
    // --- end NYI ---

    Scene scene = new Scene(root, 350, 150);
    dialog.setScene(scene);

    save.setOnAction(e -> {
      dirtyWorkspaces.forEach(ws -> {
        bus.post(new FocusWorkspaceRequest(ws));
        bus.post(new WorkspaceSaveRequest());
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