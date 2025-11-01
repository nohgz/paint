package net.cnoga.paint.core.fxml_controllers.window;

import javafx.fxml.FXML;
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
 *
 *
 */
public class ShortcutBarController extends EventBusPublisher {
  @FXML
  private void onNewFile() {
    bus.post(new ShowNewWorkspacePopupRequest());
  }

  @FXML
  private void onFileOpen() {
    bus.post(new FileOpenRequest());
  }

  @FXML
  private void onFileSave() {
    bus.post(new WorkspaceSaveRequest());
  }

  @FXML
  private void onFileSaveAs() {
    bus.post(new WorkspaceSaveAsRequest());
  }

  @FXML
  private void onClearWorkspace() {
    bus.post(new ShowClearWorkspacePopupRequest());
  }
}
