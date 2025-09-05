package net.cnoga.paint.controllers.window;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.response.FileOpenedEvent;

/**
 * Controller for the bottom information panel.
 * <p>
 * Displays contextual information about the canvas, such as its size,
 * the currently selected tool, cursor position, and zoom level.
 * @author cnoga
 * @version 1.0
 */
@EventBusSubscriber
public class BottomInfoController extends EventBusPublisher {
  public Label textStatus;

  @FXML
  private void initialize() {
    bus.register(this);
  }

  @SubscribeEvent
  private void onFileOpened(FileOpenedEvent event) {
    textStatus.setText(event.file().getName());
  }
}
