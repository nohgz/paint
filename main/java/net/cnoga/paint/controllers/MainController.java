package net.cnoga.paint.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.listener.SubWindowListener;
import net.cnoga.paint.listener.WorkspaceListener;
import net.cnoga.paint.publisher.FileIOPublisher;
import net.cnoga.paint.publisher.SubwindowPublisher;

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
 *   <li>Creates and registers services such as {@link FileIOPublisher}
 *       and {@link WorkspaceListener}.</li>
 *   <li>Provides glue code between the UI and the event bus.</li>
 * </ul>
 *
 * <p>
 * Note: This class currently mixes concerns (service setup,
 * controller wiring, and event-bus registration) and may be
 * refactored into a cleaner separation of responsibilities.
 * </p>
 *
 * @author cnoga
 * @version 1.0
 */
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
  private WorkspaceController workspaceController;

  private WorkspaceListener workspaceListener;
  private FileIOPublisher fileIOPublisher;
  private SubwindowPublisher subwindowPublisher;
  private SubWindowListener subwindowListener;

  /**
   * Initializes the main controller and wires up the application.
   * <p>
   * This method creates the event bus, sets up core services, and
   * connects subcontrollers to their publishers. It should be called
   * once when the application starts.
   * </p>
   *
   * @param primaryStage the primary JavaFX stage
   * @param primaryScene the primary JavaFX scene
   */
  public void init(Stage primaryStage, Scene primaryScene) {
    EventBus bus = new EventBus();

    // The services the program runs
    workspaceListener = workspaceController.initWorkspaceListener();
    subwindowListener = rightTopbarController.initSubWindowListener();

    subwindowListener.initSubwindowListener(primaryStage);
    // Initialize. These methods are not tied to the service but instead
    // act was ways for fxml to communicate with the controllers.
    fileIOPublisher = new FileIOPublisher(bus, primaryStage);
    subwindowPublisher = new SubwindowPublisher(bus);

    shortcutBarController.initFileIOPublisher(fileIOPublisher);
    leftTopbarController.initFileIOPublisher(fileIOPublisher);
    rightTopbarController.initSubWindowPublisher(subwindowPublisher);

    // Then register each of these events and services to the bus.
    // I might make when you create a listener, it will immediately register.
    bus.register(workspaceListener);
    bus.register(subwindowListener);

    // intercept the main close request
    primaryStage.setOnCloseRequest(windowEvent -> {
      windowEvent.consume();
      fileIOPublisher.closeProgram();
    });
  }
}
