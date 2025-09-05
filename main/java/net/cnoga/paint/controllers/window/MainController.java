package net.cnoga.paint.controllers.window;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.events.request.CloseProgramRequest;
import net.cnoga.paint.events.request.NewFileRequest;
import net.cnoga.paint.events.request.OpenColorPickerRequest;
import net.cnoga.paint.events.response.ToolChangedEvent;
import net.cnoga.paint.service.FileIOService;
import net.cnoga.paint.service.ProgramService;
import net.cnoga.paint.service.SubWindowService;
import net.cnoga.paint.service.WorkspaceService;
import net.cnoga.paint.tool.PaintTools;

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
 *   <li>Creates and registers services such as {@link FileIOService}
 *       and {@link WorkspaceService}.</li>
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
  private net.cnoga.paint.service.SubWindowService subwindowService;

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
  public void init(Stage primaryStage) {
    // The services the program runs
    workspaceService = new WorkspaceService();
    programService = new ProgramService(primaryStage);
    fileIOService = new FileIOService(primaryStage);
    subwindowService = new SubWindowService(primaryStage);

    // Connect the stuff in the program to the services
    workspaceController.initWorkspaceService();
    rightTopbarController.initSubWindowService();

    // Initial startup state
    bus.post(new ToolChangedEvent(PaintTools.BRUSH));
    bus.post(new NewFileRequest());

    // intercept the main close request
    primaryStage.setOnCloseRequest(windowEvent -> {
      windowEvent.consume();
      bus.post(new CloseProgramRequest());
    });
  }
}
