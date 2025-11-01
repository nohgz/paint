package net.cnoga.paint.core.fxml_controllers.window;


import javafx.scene.control.MenuButton;
import javafx.scene.control.ToggleButton;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.events.request.InitSubWindowServiceRequest;
import net.cnoga.paint.core.bus.events.request.OpenAboutRequest;
import net.cnoga.paint.core.bus.events.request.OpenChangelogRequest;
import net.cnoga.paint.core.bus.events.request.OpenGitHubRequest;
import net.cnoga.paint.core.bus.events.request.OpenHelpRequest;
import net.cnoga.paint.core.bus.events.request.OpenSettingsRequest;
import net.cnoga.paint.core.bus.events.request.OpenToolsRequest;


/**
 * Controller for the right-side top bar, managing button actions and subwindow requests.
 */
@EventBusSubscriber
public class RightTopbarController extends EventBusPublisher {

  public ToggleButton right_topbar_tools;
  public ToggleButton right_topbar_settings;
  public MenuButton right_topbar_help;

  /** Registers this controller to the global event bus. */
  public RightTopbarController() {
    bus.register(this);
  }

  /** Opens the tools subwindow. */
  public void onOpenTools() {
    bus.post(new OpenToolsRequest());
  }

  /** Opens the settings subwindow. */
  public void onOpenSettings() {
    bus.post(new OpenSettingsRequest());
  }

  /** Opens the changelog subwindow. */
  public void onOpenChangelog() {
    bus.post(new OpenChangelogRequest());
  }

  /** Opens the help window. */
  public void onOpenHelp() {
    bus.post(new OpenHelpRequest());
  }

  /** Opens the about subwindow. */
  public void onOpenAbout() {
    bus.post(new OpenAboutRequest());
  }

  /** Opens the project’s GitHub page. */
  public void onOpenGithub() {
    bus.post(new OpenGitHubRequest());
  }

  /**
   * Initializes the subwindow service with the given top bar buttons.
   * <p>
   * This method posts an {@link InitSubWindowServiceRequest} to the event bus
   * containing references to all relevant toggle buttons.
   */
  public void initSubWindowService() {
    bus.post(
      new InitSubWindowServiceRequest(right_topbar_tools, right_topbar_settings));
  }
}
