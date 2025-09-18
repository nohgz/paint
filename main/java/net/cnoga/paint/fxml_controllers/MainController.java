package net.cnoga.paint.fxml_controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.fxml_controllers.window.BottomInfoController;
import net.cnoga.paint.fxml_controllers.window.LeftTopbarController;
import net.cnoga.paint.fxml_controllers.window.RightTopbarController;
import net.cnoga.paint.fxml_controllers.window.ShortcutBarController;
import net.cnoga.paint.fxml_controllers.window.ToolInfoController;
import net.cnoga.paint.fxml_controllers.window.WorkspaceController;
import net.cnoga.paint.events.request.CloseProgramRequest;
import net.cnoga.paint.brews.FileIOBrew;
import net.cnoga.paint.brews.KeystrokeBrew;
import net.cnoga.paint.brews.ProgramBrew;
import net.cnoga.paint.brews.SaveWarningBrew;
import net.cnoga.paint.brews.SubWindowBrew;
import net.cnoga.paint.brews.WorkspaceBrew;

/**
 * The root JavaFX controller for the application.
 * <p>
 * {@code MainController} orchestrates initialization of the primary UI components, sets up core
 * services (e.g. file I/O), and wires everything together through an {@link EventBus}.
 * </p>
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Initializes subcontrollers injected from FXML.</li>
 *   <li>Creates and registers services such as {@link WorkspaceBrew}.</li>
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

  private WorkspaceBrew workspaceBrew;
  private FileIOBrew fileIOBrew;
  private ProgramBrew programBrew;
  private SubWindowBrew subwindowBrew;
  private KeystrokeBrew keystrokeBrew;
  private SaveWarningBrew saveWarningBrew;

  /**
   * Initializes the main controller and wires up the application.
   * <p>
   * This method creates the event bus, sets up core services, and connects subcontrollers to their
   * publishers. It should be called once when the application starts.
   * </p>
   *
   * @param primaryStage the primary JavaFX stage
   */
  public void init(Stage primaryStage, Scene primaryScene) {
    // The services the program runs
    fileIOBrew = new FileIOBrew(primaryStage);
    programBrew = new ProgramBrew(primaryStage);
    workspaceBrew = new WorkspaceBrew(saveWarningBrew);
    subwindowBrew = new SubWindowBrew(primaryStage);
    keystrokeBrew = new KeystrokeBrew(primaryScene);

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
