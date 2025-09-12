package net.cnoga.paint.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.controllers.window.BottomInfoController;
import net.cnoga.paint.controllers.window.LeftTopbarController;
import net.cnoga.paint.controllers.window.RightTopbarController;
import net.cnoga.paint.controllers.window.ShortcutBarController;
import net.cnoga.paint.controllers.window.ToolInfoController;
import net.cnoga.paint.controllers.window.WorkspaceController;
import net.cnoga.paint.events.request.CloseProgramRequest;
import net.cnoga.paint.service.FileIOService;
import net.cnoga.paint.service.KeystrokeService;
import net.cnoga.paint.service.ProgramService;
import net.cnoga.paint.service.SaveWarningService;
import net.cnoga.paint.service.SubWindowService;
import net.cnoga.paint.service.WorkspaceService;

/**
 * The root JavaFX controller for the application.
 * <p>
 * {@code MainController} orchestrates initialization of the primary
 * UI components, sets up core services (e.g. file I/O), and wires
 * everything together through an {@link EventBus}.
 * </p>
 *
 * Responsibilities:
 * <ul>
 *   <li>Initializes subcontrollers injected from FXML.</li>
 *   <li>Creates and registers services such as {@link WorkspaceService}.</li>
 * </ul>
 *
 * @author cnoga
 * @version 1.1
 */
public class MainController extends EventBusPublisher {
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
  private WorkspaceController workspaceController;

  private WorkspaceService workspaceService;
  private FileIOService fileIOService;
  private ProgramService programService;
  private SubWindowService subwindowService;
  private KeystrokeService keystrokeService;
  private SaveWarningService saveWarningService;

  /**
   * Initializes the main controller and wires up the application.
   * <p>
   * This method creates the event bus, sets up core services, and
   * connects subcontrollers to their publishers. It should be called
   * once when the application starts.
   * </p>
   *
   * @param primaryStage the primary JavaFX stage
   */
  public void init(Stage primaryStage, Scene primaryScene) {
    // The services the program runs
    fileIOService = new FileIOService(primaryStage);
    saveWarningService = new SaveWarningService(primaryStage);
    programService = new ProgramService(primaryStage, saveWarningService);
    workspaceService = new WorkspaceService(saveWarningService);
    subwindowService = new SubWindowService(primaryStage);
    keystrokeService = new KeystrokeService(primaryScene);

    // Connect the stuff in the program to the services
    workspaceController.initWorkspaceService();
    rightTopbarController.initSubWindowService();

    // intercept the main close request
    primaryStage.setOnCloseRequest(windowEvent -> {
      windowEvent.consume();
      bus.post(new CloseProgramRequest());
    });
  }
}
