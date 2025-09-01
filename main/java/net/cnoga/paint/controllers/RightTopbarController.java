package net.cnoga.paint.controllers;


import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ToggleButton;
import net.cnoga.paint.listener.SubwindowListener;
import net.cnoga.paint.publisher.FileIOPublisher;
import net.cnoga.paint.publisher.SubwindowPublisher;


/**
 * The right top bar holds the palette/tools/layers toggles, and settings/documentation.
 *
 * <p>This controller serves as an interface between
 * `src/main/resources/net/cnoga/paint/fxml/right_topbar.fxml`
 * and the backend services that implement the application's functionality.
 *
 * <p>Each {@link javafx.scene.control.MenuItem} in the corresponding FXML file must have its
 * {@code fx:id} property bound to the corresponding public field in this controller, and its
 * {@code onAction} property bound to the appropriate handler method annotated with
 * {@link javafx.fxml.FXML}.</p>
 *
 * <p>How an interaction should go:</p>
 * <ol>
 *   <li>User clicks on the "color picker" icon in the menu bar.</li>
 *   <li>{@link #onOpenColorPicker(ActionEvent)} is invoked.</li>
 *   <li>The controller delegates the action to the backend service (e.g., FileService).</li>
 *   <li>The service updates the application model (e.g., {@link FileIOPublisher}).</li>
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

public class RightTopbarController {

  public ToggleButton right_topbar_color_picker;
  public ToggleButton right_topbar_tools;
  public ToggleButton right_topbar_layers;
  public Button right_topbar_settings;
  public MenuButton right_topbar_help;

  private SubwindowPublisher subwindowPublisher;

  public void initSubWindowPublisher(SubwindowPublisher subwindowPublisher) {
    this.subwindowPublisher = subwindowPublisher;
  }

  public void onOpenColorPicker(ActionEvent actionEvent) {
    // TODO: actually make this thing open a window for the color picker
    if (right_topbar_color_picker.isSelected()) {
      subwindowPublisher.toggleColorPickerWindow(true);
    } else {
      subwindowPublisher.toggleColorPickerWindow(false);
    }
  }

  public void onOpenTools(ActionEvent actionEvent) {
    // TODO: make this thing open the tools window
    if (right_topbar_tools.isSelected()) {
      subwindowPublisher.toggleToolsWindow(true);
    } else {
      subwindowPublisher.toggleToolsWindow(false);
    }
  }

  public void onOpenLayers(ActionEvent actionEvent) {
    // TODO: make this guy open the canvas layers window
    if (right_topbar_layers.isSelected()) {
      subwindowPublisher.toggleLayersWindow(true);
    } else {
      subwindowPublisher.toggleLayersWindow(false);
    }
  }

  public void onOpenHistory(ActionEvent actionEvent) {
    // TODO: make this guy open the history window
    if (right_topbar_layers.isSelected()) {
      subwindowPublisher.toggleHistoryWindow(true);
    } else {
      subwindowPublisher.toggleHistoryWindow(false);
    }
  }

  public void onOpenSettings(ActionEvent actionEvent) {
    // TODO: Will open settings window
  }

  public void onOpenHelp(ActionEvent actionEvent) {
    // TODO: Will have help, documentation, and patch notes.
  }

  public void onOpenThemes(ActionEvent actionEvent) {
    // TODO: Will open the themes window
  }
}
