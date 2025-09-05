package net.cnoga.paint.controllers.window;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.events.request.FileOpenRequest;
import net.cnoga.paint.events.request.FileSaveAsRequest;
import net.cnoga.paint.events.request.FileSaveRequest;
import net.cnoga.paint.events.request.CloseProgramRequest;
import net.cnoga.paint.events.request.NewFileRequest;

/**
 * The left top bar is the File/Edit/View/Image bar.
 *
 * <p>This controller serves as an interface between
 * `src/main/resources/net/cnoga/paint/fxml/left_topbar.fxml`
 * and the backend services that implement the application's functionality.
 *
 * <p>Each {@link MenuItem} in the corresponding FXML file must have its
 * {@code fx:id} property bound to the corresponding public field in this controller, and its
 * {@code onAction} property bound to the appropriate handler method annotated with
 * {@link FXML}.</p>
 *
 * <p>How an interaction should go:</p>
 * <ol>
 *   <li>User clicks "Open File" in the menu bar.</li>
 *   <li>The controller publishes the OpenFileEvent to the Event bus.</li>
 * </ol>
 *
 * <p>Controller Responsibilities:</p>
 * <ul>
 *   <li>Responding to menu item actions.</li>
 *   <li>Delegating application logic to backend services.</li>
 * </ul>
 *
 * @author cnoga
 * @version 1.0
 */

public class LeftTopbarController extends EventBusPublisher {
  public MenuItem left_topbar_new_file;
  public MenuItem left_topbar_file_open;
  public MenuItem left_topbar_file_save;
  public MenuItem left_topbar_file_save_as;
  public MenuItem left_topbar_exit;

  @FXML
  private void onFileNew(ActionEvent event) {
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
  @FXML
  private void onAppExit(ActionEvent actionEvent) {
    bus.post(new CloseProgramRequest());
  }
}
