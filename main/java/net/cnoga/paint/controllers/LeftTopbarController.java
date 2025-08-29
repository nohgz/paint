package net.cnoga.paint.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

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
 *   <li>{@link #onOpenFile(ActionEvent)} is invoked.</li>
 *   <li>The controller delegates the action to the backend service (e.g., {@link net.cnoga.paint.publisher.FileIOPublisher}).</li>
 *   <li>The service updates the application model (e.g., {@link net.cnoga.paint.listener.WorkspaceListener}).</li>
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
public class LeftTopbarController extends AbstractIOController {

  public MenuItem left_topbar_new_file;
  public MenuItem left_topbar_file_open;
  public MenuItem left_topbar_file_save;
  public MenuItem left_topbar_file_save_as;
  public MenuItem left_topbar_exit;

  @FXML
  public void onAppExit(ActionEvent actionEvent) {
    fileIOPublisher.closeProgram();
  }
}
