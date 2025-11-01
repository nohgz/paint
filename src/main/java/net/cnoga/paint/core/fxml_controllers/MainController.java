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
import net.cnoga.paint.core.bus.events.request.CloseProgramRequest;
import net.cnoga.paint.core.fxml_controllers.window.RightTopbarController;
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
 */
public class MainController extends EventBusPublisher {

  /** The settings and subwindow toggle buttons. */
  @FXML
  @SuppressWarnings("unused")
  private RightTopbarController rightTopbarController;
  @FXML
  @SuppressWarnings("unused")
  private WorkspaceController workspaceController;

  /**
   * Initializes the main controller and wires up the application.
   * <p>
   * This method creates the event bus, sets up core services, and connects subcontrollers to their
   * publishers. It is called once and ONLY once when the application starts.
   * </p>
   *
   * @param primaryStage the primary JavaFX stage
   * @param primaryScene the primary JavaFX scene
   * @throws IOException for whenever the webserver blows up
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

  /**
   * Initializes the brews for the Pain(t) application.
   *
   * @param primaryStage the primary JavaFX stage
   * @param primaryScene the primary JavaFX scene
   */
  private void initializeBrews(Stage primaryStage, Scene primaryScene) {
    new FileIOBrew(primaryStage);
    new ProgramBrew(primaryStage);
    new SubWindowBrew(primaryStage);
    new KeystrokeBrew(primaryScene);
    new AutosaveBrew();
    new LoggerBrew();
    WorkspaceBrew workspaceBrew = new WorkspaceBrew();
    new SimpleWebServerBrew(new SimpleWebServer(workspaceBrew));
  }
}
