package net.cnoga.paint.controllers.window;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.events.request.FileOpenRequest;
import net.cnoga.paint.events.request.FileSaveAsRequest;
import net.cnoga.paint.events.request.FileSaveRequest;
import net.cnoga.paint.events.request.NewFileRequest;

/**
 * Controller for the shortcut bar.
 * <p>
 * Provides quick access to frequently used file operations
 * (new, open, save) for convenience.
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
    bus.post(new NewFileRequest());
  }
  @FXML
  private void onFileOpen(ActionEvent event) {
    bus.post(new FileOpenRequest());
  }
  @FXML
  private void onFileSave(ActionEvent event) {
    bus.post(new FileSaveRequest());
  }
  @FXML
  private void onFileSaveAs(ActionEvent event) {
    bus.post(new FileSaveAsRequest());
  }
}
