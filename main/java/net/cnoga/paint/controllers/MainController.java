package net.cnoga.paint.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.services.CanvasListener;
import net.cnoga.paint.services.FileIOPublisher;
import net.cnoga.paint.services.TextListener;
import net.cnoga.paint.services.ThemePublisher;

public class MainController {

  @FXML
  private ToolInfoController toolInfoController;
  @FXML
  private BottomInfoController bottomInfoController;
  @FXML
  private ShortcutBarController shortcutBarController;
  @FXML
  private LeftTopbarController leftTopbarController;
  @FXML
  private RightTopbarController rightTopbarController;
  @FXML
  private CanvasController canvasController;

  private CanvasListener canvasListener;
  private FileIOPublisher fileIOPublisher;
  private TextListener textListener;
  private ThemePublisher themePublisher;

  public void init(Stage primaryStage, Scene primaryScene) {
    // FIXME: There is a conglomeration of different ways to do things and register
    // them to the event bus based on what they do. I don't like this. aka
    // this file does way too many things at once which is *bad*

    // Custom piping stuff
    EventBus bus = new EventBus();

    // The services the program runs
    fileIOPublisher = new FileIOPublisher(bus, primaryStage);
    themePublisher = new ThemePublisher(bus);
    textListener = new TextListener();

    canvasListener = canvasController.initCanvasService();

    // Initialize. These methods are not tied to the service but instead
    // act was ways for fxml to communicate with the controllers.
    shortcutBarController.initFileIOPublisher(fileIOPublisher);
    leftTopbarController.initFileIOPublisher(fileIOPublisher);
    rightTopbarController.initThemePublisher(themePublisher);

    // Then register each of these events and services to the bus.
    // I might make when you create a bus enabled service, it will immediately register.
    bus.register(textListener);
    bus.register(canvasListener);
  }
}
