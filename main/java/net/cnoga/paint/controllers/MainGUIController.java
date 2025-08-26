package net.cnoga.paint.controllers;

import javafx.fxml.FXML;
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

  public void init(Stage primaryStage) {
    // Custom piping stuff
    EventBus bus = new EventBus();

    // The services the program runs
    fileIOService = new FileIOService(bus, primaryStage);
    new TextService(bus);

    canvasController.initCanvasService(bus);

    // IO Aware stuff
    shortcutBarController.setFileIOService(fileIOService);
    leftTopbarController.setFileIOService(fileIOService);

  }
}
