package net.cnoga.paint.core.fxml_controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.cnoga.paint.core.brews.AutosaveBrew;
import net.cnoga.paint.core.brews.FileIOBrew;
import net.cnoga.paint.core.brews.KeystrokeBrew;
import net.cnoga.paint.core.brews.LoggerBrew;
import net.cnoga.paint.core.brews.ProgramBrew;
import net.cnoga.paint.core.brews.SimpleWebServerBrew;
import net.cnoga.paint.core.brews.SubWindowBrew;
import net.cnoga.paint.core.brews.WorkspaceBrew;
import net.cnoga.paint.core.bus.EventBus;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.events.request.ChangeThemeRequest;
import net.cnoga.paint.core.bus.events.request.CloseProgramRequest;
import net.cnoga.paint.core.fxml_controllers.window.BottomInfoController;
import net.cnoga.paint.core.fxml_controllers.window.LeftTopbarController;
import net.cnoga.paint.core.fxml_controllers.window.RightTopbarController;
import net.cnoga.paint.core.fxml_controllers.window.ShortcutBarController;
import net.cnoga.paint.core.fxml_controllers.window.ToolInfoController;
import net.cnoga.paint.core.fxml_controllers.window.WorkspaceController;
import net.cnoga.paint.server.SimpleWebServer;

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

  /**
   * Initializes the main controller and wires up the application.
   * <p>
   * This method creates the event bus, sets up core services, and connects subcontrollers to their
   * publishers. It should be called once when the application starts.
   * </p>
   *
   * @param primaryStage the primary JavaFX stage
   */
  public void init(Stage primaryStage, Scene primaryScene) throws IOException {
    initializeBrews(primaryStage, primaryScene);

    // Connect the stuff in the program to the services
    workspaceController.initWorkspaceService();
    rightTopbarController.initSubWindowService();

    // intercept the main close request
    primaryStage.setOnCloseRequest(windowEvent -> {
      windowEvent.consume();
      bus.post(new CloseProgramRequest());
    });
  }

  private void initializeBrews(Stage primaryStage, Scene primaryScene) throws IOException {
    FileIOBrew fileIOBrew = new FileIOBrew(primaryStage);
    ProgramBrew programBrew = new ProgramBrew(primaryStage);
    WorkspaceBrew workspaceBrew = new WorkspaceBrew();
    SubWindowBrew subwindowBrew = new SubWindowBrew(primaryStage);
    KeystrokeBrew keystrokeBrew = new KeystrokeBrew(primaryScene);
    AutosaveBrew autosaveBrew = new AutosaveBrew();
    LoggerBrew loggerBrew = new LoggerBrew();
    SimpleWebServerBrew simpleWebServerBrew = new SimpleWebServerBrew(
      new SimpleWebServer(workspaceBrew));
  }
}
