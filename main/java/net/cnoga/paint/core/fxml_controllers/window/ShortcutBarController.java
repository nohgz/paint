package net.cnoga.paint.core.fxml_controllers.window;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.events.request.FileOpenRequest;
import net.cnoga.paint.core.bus.events.request.ShowClearWorkspacePopupRequest;
import net.cnoga.paint.core.bus.events.request.ShowNewWorkspacePopupRequest;
import net.cnoga.paint.core.bus.events.request.WorkspaceSaveAsRequest;
import net.cnoga.paint.core.bus.events.request.WorkspaceSaveRequest;

/**
 * Controller for the shortcut bar.
 * <p>
 * Provides quick access to frequently used file operations (new, open, save) for convenience.
 *
 * @author cnoga
 * @version 1.0
 */
public class ShortcutBarController extends EventBusPublisher {

  public Button shortcut_bar_new;
  public Button shortcut_bar_open;
  public Button shortcut_bar_save;
  public Button shortcut_bar_save_as;

  @FXML
  private void onNewFile(ActionEvent event) {
    bus.post(new ShowNewWorkspacePopupRequest());
  }

  @FXML
  private void onFileOpen(ActionEvent event) {
    bus.post(new FileOpenRequest());
  }

  @FXML
  private void onFileSave(ActionEvent event) {
    bus.post(new WorkspaceSaveRequest());
  }

  @FXML
  private void onFileSaveAs(ActionEvent event) {
    bus.post(new WorkspaceSaveAsRequest());
  }

  @FXML
  private void onClearWorkspace(ActionEvent event) {
    bus.post(new ShowClearWorkspacePopupRequest());
  }
}
