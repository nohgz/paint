package net.cnoga.paint.services;

import javafx.scene.Scene;
import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.bus.EventBusProvider;
import net.cnoga.paint.events.ChangeThemeEvent;

public class ThemePublisher extends EventBusProvider {

  public ThemePublisher(EventBus bus) {
    super(bus);
  }

  public void changeTheme() {
    bus.post(new ChangeThemeEvent());
  }
}
