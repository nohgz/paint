package net.cnoga.paint.client.popup;

import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.request.FocusWorkspaceRequest;
import net.cnoga.paint.core.bus.events.request.ShowProgramSaveWarningPopupRequest;
import net.cnoga.paint.core.bus.events.request.WorkspaceSaveRequest;
import net.cnoga.paint.core.workspace.Workspace;

/**
 * Popup warning the user about unsaved changes in multiple workspaces before closing the program.
 * Provides Save, Discard, and Cancel options.
 * <p>
 * Buttons are laid out equally spaced in a horizontal box for consistent appearance across
 * dialogs.
 * </p>
 */
@EventBusSubscriber
public class ProgramSaveWarningPopup extends AbstractInputPopup {

  private final List<Workspace> dirtyWorkspaces;
  private final Runnable onCloseAll;

  public ProgramSaveWarningPopup(List<Workspace> dirtyWorkspaces, Runnable onCloseAll) {
    super("Exit Program?");
    this.dirtyWorkspaces = dirtyWorkspaces;
    this.onCloseAll = onCloseAll;
  }

  @Override
  protected Pane buildContent() {
    String msg = dirtyWorkspaces.size() == 1
      ? "This workspace has unsaved changes. What do you want to do?"
      : "You have unsaved changes in " + dirtyWorkspaces.size()
        + " workspaces. What do you want to do?";

    VBox box = new VBox(10);
    box.setAlignment(Pos.CENTER);

    Label label = new Label(msg);
    label.setWrapText(true);
    box.getChildren().add(label);

    return box;
  }

  @Override
  protected void onConfirm() {
    dirtyWorkspaces.forEach(ws -> {
      bus.post(new FocusWorkspaceRequest(ws));
      bus.post(new WorkspaceSaveRequest());
    });

    if (onCloseAll != null) {
      onCloseAll.run();
    }
  }

  @Override
  protected HBox buildButtonBar() {
    Button saveButton = new Button("Save");
    saveButton.setMaxWidth(Double.MAX_VALUE);
    saveButton.setOnAction(e -> {
      onConfirm();
      dialogStage.close();
    });

    Button discardButton = new Button(
      dirtyWorkspaces.size() == 1 ? "Discard & Exit" : "Discard All & Exit"
    );
    discardButton.setMaxWidth(Double.MAX_VALUE);
    discardButton.setOnAction(e -> {
      if (onCloseAll != null) {
        onCloseAll.run();
      }
      dialogStage.close();
    });

    Button cancelButton = new Button("Cancel");
    cancelButton.setMaxWidth(Double.MAX_VALUE);
    cancelButton.setOnAction(e -> {
      onCancel();
      dialogStage.close();
    });

    HBox bar = new HBox(10, saveButton, discardButton, cancelButton);
    bar.setStyle("-fx-alignment: center; -fx-padding: 10;");

    // make buttons grow equally
    HBox.setHgrow(saveButton, Priority.ALWAYS);
    HBox.setHgrow(discardButton, Priority.ALWAYS);
    HBox.setHgrow(cancelButton, Priority.ALWAYS);

    return bar;
  }

  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onOpen(ShowProgramSaveWarningPopupRequest req) {
    super.show();
  }
}
