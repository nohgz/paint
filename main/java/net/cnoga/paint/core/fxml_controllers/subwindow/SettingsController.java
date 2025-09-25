package net.cnoga.paint.core.fxml_controllers.subwindow;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.events.request.StartServerRequest;
import net.cnoga.paint.core.bus.events.request.StopServerRequest;

@EventBusSubscriber
public class SettingsController extends EventBusPublisher {

  public Button startServer;
  public Button openServerInBrowser;
  public Button stopServer;

  public SettingsController() {
    bus.register(this);
  }

  public void startServer(ActionEvent actionEvent) {
    bus.post(new StartServerRequest());
  }

  public void openServer(ActionEvent actionEvent) {
    System.out.println("[SettingsController] NEW THING");
  }

  public void stopServer(ActionEvent actionEvent) {
    bus.post(new StopServerRequest());
  }
}
