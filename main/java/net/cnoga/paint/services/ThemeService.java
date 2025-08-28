package net.cnoga.paint.services;

import javafx.scene.Scene;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.ChangeThemeEvent;

@EventBusSubscriber
public class ThemeService {

  private Scene scene;
  private static final String LIGHT = "/themes/light.css";
  private static final String DARK  = "/themes/dark.css";

  private boolean isDarkMode = false;

  public void initThemeService(Scene scene) {
    this.scene = scene;
  }

  @SubscribeEvent
  public void changeTheme(ChangeThemeEvent changeThemeEvent) {
    System.out.println("I'm the theme service and I'm trying to change the theme!");
    scene.getStylesheets().clear();
    if (isDarkMode) {
      scene.getStylesheets().add(LIGHT);
    } else {
      scene.getStylesheets().add(DARK);
    }
    isDarkMode = !isDarkMode;
  }
}
