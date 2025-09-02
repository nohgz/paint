package net.cnoga.paint.publisher;

import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.bus.EventBusProvider;
import net.cnoga.paint.events.ClickColorPickerEvent;
import net.cnoga.paint.events.ClickHistoryEvent;
import net.cnoga.paint.events.ClickLayersEvent;
import net.cnoga.paint.events.ClickSettingsEvent;
import net.cnoga.paint.events.ClickToolsEvent;

public class SubwindowPublisher extends EventBusProvider {

  public SubwindowPublisher(EventBus bus) {
    super(bus);
  }

  public void toggleToolsWindow() {
    bus.post(new ClickToolsEvent());
  }

  public void toggleLayersWindow() {
    bus.post(new ClickLayersEvent());
  }

  public void toggleColorPickerWindow() {
    bus.post(new ClickColorPickerEvent());
  }

  public void toggleHistoryWindow() {
    bus.post(new ClickHistoryEvent());
  }

  public void toggleSettingsWindow() {
    bus.post(new ClickSettingsEvent());
  }
}
