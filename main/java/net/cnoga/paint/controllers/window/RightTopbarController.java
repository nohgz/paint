package net.cnoga.paint.controllers.window;


import javafx.event.ActionEvent;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ToggleButton;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.events.request.OpenSettingsRequest;
import net.cnoga.paint.events.request.OpenColorPickerRequest;
import net.cnoga.paint.events.request.OpenHistoryRequest;
import net.cnoga.paint.events.request.OpenLayersRequest;
import net.cnoga.paint.events.request.OpenToolsRequest;


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
 *   <li>The service updates the application model (e.g., {@link FileIOService}).</li>
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

public class RightTopbarController extends EventBusPublisher {

  public ToggleButton right_topbar_color_picker;
  public ToggleButton right_topbar_tools;
  public ToggleButton right_topbar_layers;
  public ToggleButton right_topbar_history;
  public ToggleButton right_topbar_settings;
  public MenuButton right_topbar_help;

  public void onOpenColorPicker(ActionEvent actionEvent) {
    bus.post(new OpenColorPickerRequest());
  }

  public void onOpenTools(ActionEvent actionEvent) {
    bus.post(new OpenToolsRequest());
  }

  public void onOpenLayers(ActionEvent actionEvent) {
    bus.post(new OpenLayersRequest());
  }

  public void onOpenHistory(ActionEvent actionEvent) {
    bus.post(new OpenHistoryRequest());
  }

  public void onOpenSettings(ActionEvent actionEvent) {
    bus.post(new OpenSettingsRequest());
  }

  public void onOpenHelp(ActionEvent actionEvent) {
    // TODO: Will have help, documentation, and patch notes.
  }
}
