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

  public void toggleToolsWindow(Boolean state) {
    bus.post(new ToggleToolsEvent(state));
  }

  public void toggleLayersWindow(Boolean state) {
    bus.post(new ToggleLayersEvent(state));
  }

  public void toggleColorPickerWindow(Boolean state) {
    bus.post(new ToggleColorPickerEvent(state));
  }

  public void toggleHistoryWindow(Boolean state) {
    bus.post(new ToggleHistoryEvent(state));
  }
}
