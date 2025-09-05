package net.cnoga.paint.controllers.window;

import javafx.scene.control.Label;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.response.ToolChangedEvent;

/**
 * Controller for the tool info panel.
 * <p>
 * Displays information about the currently selected tool and may
 * provide an interactable interface for configuring the tool.
 */
@EventBusSubscriber
public class ToolInfoController extends EventBusPublisher {

  public ToolInfoController() {
    bus.register(this);
  }

  public Label toolInfoCaption;

  @SubscribeEvent
  private void updateToolInformation(ToolChangedEvent event) {
    toolInfoCaption.setText(event.tool().getName());
  }


}
