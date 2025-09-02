package net.cnoga.paint.publisher;

import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.bus.EventBusProvider;
import net.cnoga.paint.events.ToggleColorPickerEvent;
import net.cnoga.paint.events.ToggleHistoryEvent;
import net.cnoga.paint.events.ToggleLayersEvent;
import net.cnoga.paint.events.ToggleToolsEvent;

public class SubwindowPublisher extends EventBusProvider {

  public SubwindowPublisher(EventBus bus) {
    super(bus);
  }

  public void toggleToolsWindow() {
    bus.post(new ToggleToolsEvent());
  }

  public void toggleLayersWindow() {
    bus.post(new ToggleLayersEvent());
  }

  public void toggleColorPickerWindow() {
    bus.post(new ToggleColorPickerEvent());
  }

  public void toggleHistoryWindow() {
    bus.post(new ToggleHistoryEvent());
  }
}
