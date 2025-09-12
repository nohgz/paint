package net.cnoga.paint.controllers.window;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.events.request.CloseProgramRequest;
import net.cnoga.paint.events.request.FileOpenRequest;
import net.cnoga.paint.events.request.NewFileRequest;
import net.cnoga.paint.events.request.WorkspaceSaveAsRequest;
import net.cnoga.paint.events.request.WorkspaceSaveRequest;

/**
 * Controller for the left top bar, which contains the File/Edit/View/Image menus.
 *
 * <p>Acts as the bridge between {@code left_topbar.fxml} and the backend services.
 * Each menu item is wired via {@code fx:id} and {@code onAction} to this controller, which then
 * posts the corresponding request to the {@link net.cnoga.paint.bus.EventBus}.</p>
 *
 * <h3>Typical Flow:</h3>
 * <ol>
 *   <li>User selects a menu item (e.g., "Open File").</li>
 *   <li>The corresponding handler posts an event such as {@link FileOpenRequest}.</li>
 *   <li>Subscribed services react to the event and perform the actual work.</li>
 * </ol>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Listen for menu item actions.</li>
 *   <li>Post appropriate file- and app-related events.</li>
 * </ul>
 *
 * @author cnoga
 * @version 1.1
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
    bus.post(new WorkspaceSaveRequest());
  }

  @FXML
  private void onFileSaveAs(ActionEvent event) {
    bus.post(new WorkspaceSaveAsRequest());
  }

  @FXML
  private void onAppExit(ActionEvent event) {
    bus.post(new CloseProgramRequest());
  }
}
