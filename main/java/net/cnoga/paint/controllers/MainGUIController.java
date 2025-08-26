package net.cnoga.paint.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.services.CanvasService;
import net.cnoga.paint.services.FileIOService;
import net.cnoga.paint.services.TextService;

public class MainGUIController {

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

  private CanvasService canvasService;
  private FileIOService fileIOService;
  private TextService textService;

  public void init(Stage primaryStage) {
    // FIXME: There is a conglomeration of different ways to do things and register
    // them to the event bus based on what they do. I don't like this.

    // Custom piping stuff
    EventBus bus = new EventBus();

    // The services the program runs
    fileIOService = new FileIOService(bus, primaryStage);
    textService = new TextService();

    // Initialize. These methods are not tied to the service but instead
    // act was ways for fxml to communicate with the controllers.
    shortcutBarController.initFileIOService(fileIOService);
    leftTopbarController.initFileIOService(fileIOService);

    canvasService = canvasController.initCanvasService();

    // Then register each of these events and services to the bus.
    bus.register(textService);
    bus.register(canvasService);
  }
}
