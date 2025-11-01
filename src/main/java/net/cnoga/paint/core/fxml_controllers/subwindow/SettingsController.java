package net.cnoga.paint.core.fxml_controllers.subwindow;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.events.request.ChangeThemeRequest;
import net.cnoga.paint.core.bus.events.request.SetAutosaveIntervalRequest;
import net.cnoga.paint.core.bus.events.request.StartServerRequest;
import net.cnoga.paint.core.bus.events.request.StopServerRequest;
import net.cnoga.paint.core.bus.events.request.ToggleAutosaveRequest;

/**
 * Controller for the application settings panel.
 *
 * <p>Handles interactions with the settings UI, including autosave configuration,
 * theme switching, and starting/stopping the local server. Publishes corresponding
 * events to the event bus for backend handling.</p>
 *
 */
@EventBusSubscriber
public class SettingsController extends EventBusPublisher {

  /** Spinner to adjust autosave interval (in minutes). */
  public Spinner autosaveIntervalSpinner;

  /** Tracks whether dark theme is currently active. */
  private boolean isDark = false;

  /**
   * Constructs the settings controller and registers it to the event bus.
   */
  public SettingsController() {
    bus.register(this);
  }

  /**
   * Opens the given URL in the system default web browser.
   *
   * @param url the URL to open
   */
  public static void openLink(String url) {
    if (Desktop.isDesktopSupported()) {
      new Thread(() -> {
        try {
          Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
          e.printStackTrace();
        }
      }).start();
    } else {
      System.err.println("Desktop browsing not supported.");
    }
  }

  /**
   * Initializes the controller after FXML injection.
   *
   * <p>Sets up the autosave interval spinner to post {@link SetAutosaveIntervalRequest}
   * events whenever its value changes.</p>
   */
  @FXML
  private void initialize() {
    autosaveIntervalSpinner.setEditable(true);
    autosaveIntervalSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
      bus.post(new SetAutosaveIntervalRequest((int) newVal));
    });
  }

  /**
   * Starts the local paint server.
   *
   * <p>Posts a {@link StartServerRequest} to the event bus.</p>
   */
  public void startServer() {
    bus.post(new StartServerRequest());
  }

  /**
   * Opens the local server in the system browser.
   */
  public void openServer() {
    openLink("http://localhost:25565/");
  }

  /**
   * Stops the local paint server.
   */
  public void stopServer() {
    bus.post(new StopServerRequest());
  }

  /**
   * Toggles the autosave feature on or off.
   */
  public void toggleAutosave() {
    bus.post(new ToggleAutosaveRequest());
  }

  /**
   * Switches the application theme between dark (mocha) and light (latte) modes.
   */
  public void toggleTheme() {
    isDark = !isDark;
    if (isDark) {
      bus.post(new ChangeThemeRequest("/net/cnoga/paint/themes/mocha.css"));
    } else {
      bus.post(new ChangeThemeRequest("/net/cnoga/paint/themes/latte.css"));
    }
  }
}
