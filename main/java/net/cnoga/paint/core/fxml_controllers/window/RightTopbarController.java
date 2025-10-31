package net.cnoga.paint.core.fxml_controllers.window;


import javafx.scene.control.MenuButton;
import javafx.scene.control.ToggleButton;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.events.init.InitSubWindowServiceRequest;
import net.cnoga.paint.core.bus.events.request.OpenAboutRequest;
import net.cnoga.paint.core.bus.events.request.OpenChangelogRequest;
import net.cnoga.paint.core.bus.events.request.OpenGitHubRequest;
import net.cnoga.paint.core.bus.events.request.OpenHelpRequest;
import net.cnoga.paint.core.bus.events.request.OpenHistoryRequest;
import net.cnoga.paint.core.bus.events.request.OpenLayersRequest;
import net.cnoga.paint.core.bus.events.request.OpenSettingsRequest;
import net.cnoga.paint.core.bus.events.request.OpenToolsRequest;


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
@EventBusSubscriber
public class RightTopbarController extends EventBusPublisher {

  public ToggleButton right_topbar_tools;
  public ToggleButton right_topbar_layers;
  public ToggleButton right_topbar_history;
  public ToggleButton right_topbar_settings;
  public MenuButton right_topbar_help;

  public RightTopbarController() {
    bus.register(this);
  }

  public void onOpenTools() {
    bus.post(new OpenToolsRequest());
  }

  public void onOpenLayers() {
    bus.post(new OpenLayersRequest());
  }

  public void onOpenHistory() {
    bus.post(new OpenHistoryRequest());
  }

  public void onOpenSettings() {
    bus.post(new OpenSettingsRequest());
  }

  public void onOpenChangelog() {
    bus.post(new OpenChangelogRequest());
  }

  public void onOpenHelp() {
    bus.post(new OpenHelpRequest());
  }

  public void onOpenAbout() {
    bus.post(new OpenAboutRequest());
  }

  public void onOpenGithub() {
    bus.post(new OpenGitHubRequest());
  }

  public void initSubWindowService() {
    bus.post(
      new InitSubWindowServiceRequest(right_topbar_history, right_topbar_tools, right_topbar_layers,
        right_topbar_settings));
  }
}
