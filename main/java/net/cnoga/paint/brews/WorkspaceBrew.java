package net.cnoga.paint.brews;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.init.InitWorkspaceServiceRequest;
import net.cnoga.paint.events.request.ClearWorkspaceRequest;
import net.cnoga.paint.events.request.CloseCurrentWorkspaceRequest;
import net.cnoga.paint.events.request.FocusWorkspaceRequest;
import net.cnoga.paint.events.request.GetDirtyWorkspacesRequest;
import net.cnoga.paint.events.request.NewWorkspaceRequest;
import net.cnoga.paint.events.request.WorkspaceSaveAsRequest;
import net.cnoga.paint.events.request.WorkspaceSaveRequest;
import net.cnoga.paint.events.response.FileOpenedEvent;
import net.cnoga.paint.events.response.GotDirtyWorkspacesEvent;
import net.cnoga.paint.events.response.ToolChangedEvent;
import net.cnoga.paint.events.response.WorkspaceSavedAsEvent;
import net.cnoga.paint.events.response.WorkspaceSavedEvent;
import net.cnoga.paint.popup.ClearWorkspacePopup;
import net.cnoga.paint.popup.NewWorkspacePopup;
import net.cnoga.paint.popup.WorkspaceSaveWarningPopup;
import net.cnoga.paint.tool.PanTool;
import net.cnoga.paint.tool.Tool;
import net.cnoga.paint.workspace.Workspace;

/**
 * Service that manages the lifecycle and state of {@link Workspace} instances.
 * <p>
 * This class is responsible for creating, opening, closing, and saving workspaces, as well as
 * maintaining the {@link TabPane} UI that holds them.
 * </p>
 */
@EventBusSubscriber
public class WorkspaceBrew extends EventBusPublisher {

  private final List<Workspace> workspaces = new ArrayList<>();
  private WorkspaceSaveWarningPopup workspaceSaveWarningPopup;
  private TabPane workspaceTabPane;
  private Tool currentTool;

  /**
   * Constructs a {@code WorkspaceService}.
   */
  public WorkspaceBrew() {
    bus.register(this);
    new NewWorkspacePopup();
    new ClearWorkspacePopup();
  }

  /**
   * Brings the requested workspace to the front.
   *
   * @param req the event containing the workspace to focus
   */
  @SubscribeEvent
  private void onFocusWorkspace(FocusWorkspaceRequest req) {
    int index = workspaces.indexOf(req.workspace());
    if (index >= 0) {
      workspaceTabPane.getSelectionModel().select(index);
    }
  }

  /**
   * Initializes this service with the {@link TabPane} that holds workspaces.
   *
   * @param req the event containing the tab pane
   */
  @SubscribeEvent
  private void onInitWorkspaceService(InitWorkspaceServiceRequest req) {
    this.workspaceTabPane = req.tabPane();
  }

  /**
   * Sets up mouse handling for a workspace canvas, binding drawing operations to the currently
   * selected tool.
   *
   * @param ws the workspace whose canvas will be initialized
   */
  private void initCanvasForWorkspace(Workspace ws) {
    Canvas baseLayer = ws.getBaseLayer();
    Canvas effectsLayer = ws.getEffectsLayer();

    GraphicsContext base_gc = baseLayer.getGraphicsContext2D();
    GraphicsContext effects_gc = effectsLayer.getGraphicsContext2D();

    baseLayer.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
      currentTool.handleMousePressed(base_gc, effects_gc, e.getX(), e.getY());
      if (!(currentTool instanceof PanTool)) {
        ws.setDirty(true);
      }
    });

    baseLayer.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
      currentTool.handleMouseDragged(base_gc, effects_gc, e.getX(), e.getY());
      if (!(currentTool instanceof PanTool)) {
        ws.setDirty(true);
      }
    });

    baseLayer.addEventHandler(MouseEvent.MOUSE_RELEASED,
      e -> currentTool.handleMouseReleased(base_gc, effects_gc, e.getX(), e.getY()));
  }

  /**
   * Creates a new empty workspace with a white background.
   *
   * @param req event requesting a new workspace
   */
  @SubscribeEvent
  private void onNewWorkspace(NewWorkspaceRequest req) {
    Workspace ws = new Workspace("Workspace " + workspaces.size(), req.width(), req.height());
    GraphicsContext gc = ws.getBaseLayer().getGraphicsContext2D();
    gc.setFill(Color.WHITE);
    gc.fillRect(0, 0, req.width(), req.height());

    initCanvasForWorkspace(ws);
    addWorkspaceTab(ws);
  }

  @SubscribeEvent
  private void onClearWorkspace(ClearWorkspaceRequest req) {
    Workspace ws = getActiveWorkspace();
    if (ws == null) {
      return;
    }

    Canvas ca = ws.getBaseLayer();
    GraphicsContext gc = ca.getGraphicsContext2D();

    gc.setFill(Color.WHITE);
    gc.fillRect(0, 0, ca.getWidth(), ca.getHeight());

    ws.setDirty(true);
  }

  /**
   * Creates a workspace from a file (e.g., opening an image).
   *
   * @param evt event containing the file to open
   * @throws FileNotFoundException if the file cannot be read
   */
  @SubscribeEvent
  private void onFileOpened(FileOpenedEvent evt) throws FileNotFoundException {
    Image img = new Image(new FileInputStream(evt.file()));
    Workspace ws = new Workspace(evt.file().getName(), img.getWidth(), img.getHeight());
    ws.getBaseLayer().getGraphicsContext2D().drawImage(img, 0, 0);
    ws.setFile(evt.file());

    initCanvasForWorkspace(ws);
    addWorkspaceTab(ws);
  }

  /**
   * Handles closing the currently active workspace, prompting the user if it has unsaved changes.
   *
   * @param req event requesting to close the active workspace
   */
  @SubscribeEvent
  private void onCloseCurrentWorkspace(CloseCurrentWorkspaceRequest req) {
    Workspace ws = getActiveWorkspace();
    if (ws == null) {
      return;
    }

    Runnable closeWorkspace = () -> {
      workspaces.remove(ws);
      workspaceTabPane.getTabs().stream()
        .filter(tab -> tab.getContent() == ws.getScrollPane())
        .findFirst().ifPresent(tabToRemove -> workspaceTabPane.getTabs().remove(tabToRemove));
    };

    if (ws.isDirty()) {
      if (workspaceSaveWarningPopup == null) {
        workspaceSaveWarningPopup = new WorkspaceSaveWarningPopup(ws, closeWorkspace);
      }
      // set the onClose action to this current workspace
      workspaceSaveWarningPopup.setRunnable(closeWorkspace);
      workspaceSaveWarningPopup.show();
    } else {
      closeWorkspace.run();
    }
  }

  /**
   * Saves the currently active workspace. If no file is associated with it, triggers a "Save As"
   * event.
   *
   * @param req event requesting a save
   */
  @SubscribeEvent
  private void onWorkspaceSave(WorkspaceSaveRequest req) {
    Workspace ws = getActiveWorkspace();

    if (ws.getFile() != null) {
      bus.post(new WorkspaceSavedEvent(ws));
    } else {
      bus.post(new WorkspaceSavedAsEvent(ws));
    }
  }

  /**
   * Forces a "Save As" operation for the current workspace.
   *
   * @param req event requesting "Save As"
   */
  @SubscribeEvent
  private void onWorkspaceSaveAs(WorkspaceSaveAsRequest req) {
    Workspace ws = getActiveWorkspace();
    bus.post(new WorkspaceSavedAsEvent(ws));
  }

  /**
   * Updates the current tool and applies panning behavior to all workspaces when switching to or
   * from the {@link PanTool}.
   *
   * @param evt event indicating the tool change
   */
  @SubscribeEvent
  private void onToolChanged(ToolChangedEvent evt) {
    for (Workspace ws : workspaces) {
      ws.getScrollPane().setPannable(evt.tool() instanceof PanTool);
    }
    this.currentTool = evt.tool();
  }

  /**
   * Adds a workspace to the list and creates a corresponding tab in the UI.
   *
   * @param ws the workspace to add
   */
  private void addWorkspaceTab(Workspace ws) {
    workspaces.add(ws);

    Tab tab = new Tab(ws.getName(), ws.getScrollPane());
    tab.setOnCloseRequest(event -> {
      event.consume();
      bus.post(new CloseCurrentWorkspaceRequest());
    });

    workspaceTabPane.getTabs().add(tab);
    workspaceTabPane.getSelectionModel().select(tab);
  }

  /**
   * Gets the currently active workspace, or {@code null} if none is selected.
   *
   * @return the active workspace, or null
   */
  public Workspace getActiveWorkspace() {
    int index = workspaceTabPane.getSelectionModel().getSelectedIndex();
    return (index >= 0 && index < workspaces.size()) ? workspaces.get(index) : null;
  }

  /**
   * Responds to requests for dirty workspaces by posting them back on the bus.
   *
   * @param req event requesting dirty workspaces
   */
  @SubscribeEvent
  private void onGetDirtyWorkspaces(GetDirtyWorkspacesRequest req) {
    bus.post(new GotDirtyWorkspacesEvent(getDirtyWorkspaces()));
  }

  /**
   * Returns all workspaces that have unsaved changes.
   *
   * @return list of dirty workspaces
   */
  public List<Workspace> getDirtyWorkspaces() {
    return workspaces.stream().filter(Workspace::isDirty).toList();
  }

  /**
   * Returns all open workspaces.
   *
   * @return list of workspaces
   */
  public List<Workspace> getWorkspaces() {
    return workspaces;
  }
}
