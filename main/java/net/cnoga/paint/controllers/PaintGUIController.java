package net.cnoga.paint.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.cnoga.paint.services.FileIOService;
import net.cnoga.paint.services.ProgramIOService;

public class PaintGUIController {

  @FXML private ShortcutBarController shortcutBarController;
  @FXML private LeftTopbarController leftTopbarController;
  @FXML private RightTopbarController rightTopbarController;

  @FXML private CanvasController canvasController;

  private FileIOService fileIOService;
  private ProgramIOService programIOService;

  public void init(Stage primaryStage) {
    fileIOService = new FileIOService(primaryStage);
    programIOService = new ProgramIOService(primaryStage);

    // load the services when needed
    shortcutBarController.setFileIOService(fileIOService);

    leftTopbarController.setFileIOService(fileIOService);
    leftTopbarController.setProgramIOService(programIOService);

    canvasController.addImage("C:\\Users\\spoinkus\\Documents\\cs_250\\paint\\src\\main\\resources\\net\\cnoga\\paint\\testimages\\thig.png");
  }
}
