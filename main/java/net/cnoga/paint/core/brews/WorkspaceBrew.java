package net.cnoga.paint.core.brews;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import net.cnoga.paint.client.popup.ClearWorkspacePopup;
import net.cnoga.paint.client.popup.NewWorkspacePopup;
import net.cnoga.paint.client.popup.WorkspaceSaveWarningPopup;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.request.InitWorkspaceBrewRequest;
import net.cnoga.paint.core.bus.events.request.ClearWorkspaceRequest;
import net.cnoga.paint.core.bus.events.request.CloseCurrentWorkspaceRequest;
import net.cnoga.paint.core.bus.events.request.FocusWorkspaceRequest;
import net.cnoga.paint.core.bus.events.request.GetDirtyWorkspacesRequest;
import net.cnoga.paint.core.bus.events.request.NewWorkspaceRequest;
import net.cnoga.paint.core.bus.events.request.PasteSelectionRequest;
import net.cnoga.paint.core.bus.events.request.TransformWorkspaceRequest;
import net.cnoga.paint.core.bus.events.request.WorkspaceSaveAsRequest;
import net.cnoga.paint.core.bus.events.request.WorkspaceSaveRequest;
import net.cnoga.paint.core.bus.events.response.FileOpenedEvent;
import net.cnoga.paint.core.bus.events.response.GotDirtyWorkspacesEvent;
import net.cnoga.paint.core.bus.events.response.SelectionPastedEvent;
import net.cnoga.paint.core.bus.events.response.ToolChangedEvent;
import net.cnoga.paint.core.bus.events.response.WorkspaceSavedAsEvent;
import net.cnoga.paint.core.bus.events.response.WorkspaceSavedEvent;
import net.cnoga.paint.core.tool.PanTool;
import net.cnoga.paint.core.tool.Tool;
import net.cnoga.paint.core.workspace.Workspace;

/**
 * Manages the lifecycle and state of all {@link Workspace} instances.
 * <p>
 * Handles creating, opening, closing, clearing, saving, and transforming workspaces, as well as
 * managing their tabs in the UI. Integrates with the EventBus to respond to workspace-related
 * events.
 */
@EventBusSubscriber
public class WorkspaceBrew extends EventBusPublisher {

  private final List<Workspace> workspaces = new ArrayList<>();
  private WorkspaceSaveWarningPopup workspaceSaveWarningPopup;
  private TabPane workspaceTabPane;
  private Tool currentTool;
  private double lastMouseX, lastMouseY;

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
   * @param req contains the workspace to focus
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
  private void onInitWorkspaceBrew(InitWorkspaceBrewRequest req) {
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

    baseLayer.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
      lastMouseX = e.getX();
      lastMouseY = e.getY();
    });
  }

  /**
   * Handles pasting at the last mouse location.
   */
  @SubscribeEvent
  private void onPasteSelection(PasteSelectionRequest req) {
    bus.post(new SelectionPastedEvent(lastMouseX, lastMouseY));
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

  /**
   * Clears the currently active workspace.
   */
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
   * Opens a workspace from a file.
   *
   * @param evt contains the file to open
   * @throws FileNotFoundException if file cannot be read
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
   * Closes the currently active workspace, prompting to save if dirty.
   *
   * @param req event requesting close
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
   * Saves the currently active workspace or triggers "Save As".
   *
   * @param req event requesting save
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
   * Forces "Save As" for the current workspace.
   *
   * @param req event requesting save as
   */
  @SubscribeEvent
  private void onWorkspaceSaveAs(WorkspaceSaveAsRequest req) {
    Workspace ws = getActiveWorkspace();
    bus.post(new WorkspaceSavedAsEvent(ws));
  }

  /**
   * Updates the current tool and scroll behavior for PanTool.
   *
   * @param evt tool change event
   */
  @SubscribeEvent
  private void onToolChanged(ToolChangedEvent evt) {
    for (Workspace ws : workspaces) {
      ws.getScrollPane().setPannable(evt.tool() instanceof PanTool);
    }
    this.currentTool = evt.tool();
  }

  /**
   * Transforms the active workspace (rotate/mirror).
   *
   * @param req contains degrees and mirroring options
   */
  @SubscribeEvent
  private void onTransformWorkspace(TransformWorkspaceRequest req) {
    Workspace ws = getActiveWorkspace();
    if (ws == null) {
      return;
    }

    int degrees = ((req.degrees() % 360) + 360) % 360;
    if (degrees != 0 && degrees != 90 && degrees != 180 && degrees != 270) {
      System.err.println("Unsupported rotation: " + degrees);
      return;
    }

    // Rotate all layers in-place
    for (Canvas layer : ws.getLayers()) {
      rotateAndMirrorInPlace(layer, degrees, req.mirrorX(), req.mirrorY());
    }

    // Refresh layout (important when size changes)
    ws.getScrollPane().requestLayout();

    ws.setDirty(true);
  }


  /**
   * Rotates and mirrors a canvas in place.
   * <p>
   * This method resizes the canvas buffer if necessary and applies rotation and mirroring without
   * replacing the canvas node.
   *
   * @param canvas  the {@link Canvas} to transform
   * @param degrees rotation in degrees (90 degree increments)
   * @param mirrorX whether to mirror horizontally
   * @param mirrorY whether to mirror vertically
   */
  private void rotateAndMirrorInPlace(Canvas canvas, int degrees, boolean mirrorX,
    boolean mirrorY) {
    double w = canvas.getWidth();
    double h = canvas.getHeight();

    SnapshotParameters params = new SnapshotParameters();
    params.setFill(Color.TRANSPARENT);

    Image snapshot = canvas.snapshot(params, null);
    GraphicsContext gc = canvas.getGraphicsContext2D();

    // Compute new dimensions
    double newW = (degrees == 90 || degrees == 270) ? h : w;
    double newH = (degrees == 90 || degrees == 270) ? w : h;

    // Resize canvas *in place* (this does NOT replace it, just resizes its buffer)
    canvas.setWidth(newW);
    canvas.setHeight(newH);

    gc.setTransform(1, 0, 0, 1, 0, 0);
    gc.clearRect(0, 0, newW, newH);

    gc.save();

    // Set up transforms based on rotation
    switch (degrees) {
      case 90 -> {
        gc.translate(newW, 0);
        gc.rotate(90);
      }
      case 180 -> {
        gc.translate(newW, newH);
        gc.rotate(180);
      }
      case 270 -> {
        gc.translate(0, newH);
        gc.rotate(270);
      }
      default -> {
      } // 0° rotation does nothing
    }

    // Apply mirrors
    double scaleX = mirrorX ? -1 : 1;
    double scaleY = mirrorY ? -1 : 1;
    gc.scale(scaleX, scaleY);

    // Adjust for mirroring (keeps origin consistent)
    if (mirrorX) {
      gc.translate(-w, 0);
    }
    if (mirrorY) {
      gc.translate(0, -h);
    }

    gc.drawImage(snapshot, 0, 0);
    gc.restore();
  }

  /**
   * Adds a workspace to the internal list and creates a corresponding tab in the UI.
   *
   * @param ws the {@link Workspace} to add
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
   * Returns the currently active workspace, or {@code null} if no workspace is selected.
   *
   * @return the active {@link Workspace}, or {@code null} if none
   */
  public Workspace getActiveWorkspace() {
    int index = workspaceTabPane.getSelectionModel().getSelectedIndex();
    return (index >= 0 && index < workspaces.size()) ? workspaces.get(index) : null;
  }

  /**
   * Handles a request to retrieve all dirty (unsaved) workspaces and posts them on the EventBus.
   *
   * @param req the {@link GetDirtyWorkspacesRequest} event
   */
  @SubscribeEvent
  private void onGetDirtyWorkspaces(GetDirtyWorkspacesRequest req) {
    bus.post(new GotDirtyWorkspacesEvent(getDirtyWorkspaces()));
  }

  /**
   * Returns all workspaces that have unsaved changes.
   *
   * @return a list of dirty {@link Workspace} instances
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
