package net.cnoga.paint.core.fxml_controllers.window;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.response.FileOpenedEvent;
import net.cnoga.paint.core.bus.events.response.ToolChangedEvent;

/**
 * Controller for the bottom information panel of the workspace UI.
 *
 * <p>Displays contextual information such as the current file name,
 * canvas size, selected tool, cursor position, or zoom level. The panel updates reactively through
 * events published on the global {@link net.cnoga.paint.core.bus.EventBus}.</p>
 *
 * <h3>Event Handling:</h3>
 * <ul>
 *   <li>{@link FileOpenedEvent} → Updates the panel to show the opened file name.</li>
 * </ul>
 *
 * @author cnoga
 * @version 1.1
 */
@EventBusSubscriber
public class BottomInfoController extends EventBusPublisher {

  public Label textStatus;

  @FXML
  private void initialize() {
    bus.register(this);
  }

  @SubscribeEvent
  private void onToolChanged(ToolChangedEvent evt) {
    textStatus.setText(evt.tool().getHelpInfo());
  }
}
