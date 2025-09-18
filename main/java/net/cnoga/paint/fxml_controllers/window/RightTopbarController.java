package net.cnoga.paint.fxml_controllers.window;


import javafx.event.ActionEvent;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ToggleButton;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.events.init.InitSubWindowServiceRequest;
import net.cnoga.paint.events.request.OpenAboutRequest;
import net.cnoga.paint.events.request.OpenChangelogRequest;
import net.cnoga.paint.events.request.OpenGitHubRequest;
import net.cnoga.paint.events.request.OpenHelpRequest;
import net.cnoga.paint.events.request.OpenHistoryRequest;
import net.cnoga.paint.events.request.OpenLayersRequest;
import net.cnoga.paint.events.request.OpenSettingsRequest;
import net.cnoga.paint.events.request.OpenToolsRequest;


/**
 * Controller for the right-side top bar of the application UI.
 *
 * <p>The right top bar provides quick access to tools, layers, history, settings, and help
 * sections. This controller acts as the intermediary between the {@code right_topbar.fxml} layout
 * and the backend services, ensuring that user interactions (button presses and menu selections)
 * are properly dispatched as events.</p>
 *
 * <p>Responsibilities include:</p>
 * <ul>
 *   <li>Handling user interactions with the top bar menu and toggle buttons.</li>
 *   <li>Delegating application logic to backend services via an event bus.</li>
 *   <li>Initializing sub-window services with UI component references.</li>
 * </ul>
 *
 * @author cnoga
 * @version 1.0
 */
public class RightTopbarController extends EventBusPublisher {

  public ToggleButton right_topbar_tools;
  public ToggleButton right_topbar_layers;
  public ToggleButton right_topbar_history;
  public ToggleButton right_topbar_settings;
  public MenuButton right_topbar_help;

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

  public void onOpenChangelog(ActionEvent actionEvent) {
    bus.post(new OpenChangelogRequest());
  }

  public void onOpenHelp(ActionEvent actionEvent) {
    bus.post(new OpenHelpRequest());
  }

  public void onOpenAbout(ActionEvent actionEvent) {
    bus.post(new OpenAboutRequest());
  }

  public void onOpenGithub(ActionEvent actionEvent) {
    bus.post(new OpenGitHubRequest());
  }

  public void initSubWindowService() {
    bus.post(
      new InitSubWindowServiceRequest(right_topbar_history, right_topbar_tools, right_topbar_layers,
        right_topbar_settings));
  }
}
