package net.cnoga.paint.client.popup;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.request.ShowWorkspaceSaveWarningPopupRequest;
import net.cnoga.paint.core.bus.events.request.WorkspaceSaveRequest;
import net.cnoga.paint.core.workspace.Workspace;

/**
 * Popup warning the user about unsaved changes in a single workspace.
 * <p>
 * Provides three actions: Save, Discard, or Cancel. Buttons are laid out equally spaced in a
 * horizontal box for consistency across popups.
 * </p>
 */
@EventBusSubscriber
public class WorkspaceSaveWarningPopup extends AbstractInputPopup {

  private Runnable onClose;

  public WorkspaceSaveWarningPopup(Workspace ws, Runnable onClose) {
    super("Unsaved Changes");
    this.onClose = onClose;

    if (!ws.isDirty()) {
      onClose.run();
    }
  }

  @Override
  protected Pane buildContent() {
    Pane pane = new Pane();
    pane.getChildren()
      .add(new Label("This workspace has unsaved changes. What do you want to do?"));
    return pane;
  }

  @Override
  protected void onConfirm() {
    bus.post(new WorkspaceSaveRequest());
  }

  protected void onDiscard() {
    onClose.run();
  }

  @Override
  protected HBox buildButtonBar() {
    Button saveButton = new Button("Save");
    saveButton.setMaxWidth(Double.MAX_VALUE);
    saveButton.setOnAction(e -> {
      onConfirm();
      dialogStage.close();
    });

    Button discardButton = new Button("Discard");
    discardButton.setMaxWidth(Double.MAX_VALUE);
    discardButton.setOnAction(e -> {
      onDiscard();
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

  public void setRunnable(Runnable closeWorkspace) {
    this.onClose = closeWorkspace;
  }

  @SubscribeEvent
  @SuppressWarnings("unused")
  protected void onOpen(ShowWorkspaceSaveWarningPopupRequest req) {
    show();
  }
}
