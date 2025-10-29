package net.cnoga.paint.core.fxml_controllers.subwindow;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.events.request.ChangeThemeRequest;
import net.cnoga.paint.core.bus.events.request.SetAutosaveIntervalRequest;
import net.cnoga.paint.core.bus.events.request.StartServerRequest;
import net.cnoga.paint.core.bus.events.request.StopServerRequest;
import net.cnoga.paint.core.bus.events.request.ToggleAutosaveRequest;

@EventBusSubscriber
public class SettingsController extends EventBusPublisher {

  public Button startServer;
  public Button openServerInBrowser;
  public Button stopServer;
  public CheckBox autosaveCheckBox;
  public Spinner autosaveIntervalSpinner;
  public CheckBox darkModeCheckBox;

  public SettingsController() {
    bus.register(this);
  }

  // Convenience borrowed from ProgramBrew. should maybe put this in a
  // util class for dry
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

  @FXML
  private void initialize() {
    autosaveIntervalSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
      bus.post(new SetAutosaveIntervalRequest((int) newVal));
    });
  }

  public void startServer(ActionEvent actionEvent) {
    bus.post(new StartServerRequest());
  }

  public void openServer(ActionEvent actionEvent) {
    openLink("http://localhost:25565/");
  }

  public void stopServer(ActionEvent actionEvent) {
    bus.post(new StopServerRequest());
  }

  public void toggleAutosave(ActionEvent actionEvent) {
    bus.post(new ToggleAutosaveRequest());
  }

  private boolean isDark = false;
  public void toggleTheme() {
    isDark = !isDark;
    if (isDark) {
      bus.post(new ChangeThemeRequest("/net/cnoga/paint/themes/mocha.css"));
    } else {
      bus.post(new ChangeThemeRequest("/net/cnoga/paint/themes/latte.css"));
    }
  }
}
