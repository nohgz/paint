package net.cnoga.paint.service;


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
import net.cnoga.paint.events.request.CloseCurrentWorkspaceRequest;
import net.cnoga.paint.events.request.CloseProgramRequest;
import net.cnoga.paint.events.request.DirtyWorkspacesRequest;
import net.cnoga.paint.events.request.FocusWorkspaceRequest;
import net.cnoga.paint.events.request.WorkspaceSaveAsRequest;
import net.cnoga.paint.events.request.WorkspaceSaveRequest;
import net.cnoga.paint.events.request.NewFileRequest;
import net.cnoga.paint.events.response.FileOpenedEvent;
import net.cnoga.paint.events.response.GetDirtyWorkspaceEvent;
import net.cnoga.paint.events.response.ToolChangedEvent;
import net.cnoga.paint.events.response.WorkspaceSavedAsEvent;
import net.cnoga.paint.events.response.WorkspaceSavedEvent;
import net.cnoga.paint.tool.PanTool;
import net.cnoga.paint.tool.Tool;
import net.cnoga.paint.workspace.Workspace;

@EventBusSubscriber
public class WorkspaceService extends EventBusPublisher {
  private static final double DEFAULT_WIDTH = 800;
  private static final double DEFAULT_HEIGHT = 600;
  private final SaveWarningService saveWarningService;

  private final List<Workspace> workspaces = new ArrayList<>();
  private TabPane workspaceTabPane;
  private Tool currentTool;

  public WorkspaceService(SaveWarningService saveWarningService) {
    bus.register(this);
    this.saveWarningService = saveWarningService;
  }

  @SubscribeEvent
  private void onFocusWorkspaceRequest(FocusWorkspaceRequest req) {
    int index = workspaces.indexOf(req.workspace());
    if (index >= 0) {
      workspaceTabPane.getSelectionModel().select(index);
    }
  }

  @SubscribeEvent
  private void onInitWorkspaceService(InitWorkspaceServiceRequest req) {
    this.workspaceTabPane = req.tabPane();
  }

  private void initCanvasForWorkspace(Workspace ws) {
    Canvas baseLayer = ws.getBaseLayer();
    Canvas effectsLayer = ws.getEffectsLayer();

    GraphicsContext base_gc = baseLayer.getGraphicsContext2D();
    GraphicsContext effects_gc = effectsLayer.getGraphicsContext2D();

    baseLayer.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
      currentTool.onMousePressed(base_gc, effects_gc, e.getX(), e.getY());
      if (!(currentTool instanceof PanTool)) ws.setDirtyFlag(true);
    });

    baseLayer.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
      currentTool.onMouseDragged(base_gc, effects_gc, e.getX(), e.getY());
      if (!(currentTool instanceof PanTool)) ws.setDirtyFlag(true);
    });

    baseLayer.addEventHandler(MouseEvent.MOUSE_RELEASED,
      e -> currentTool.onMouseReleased(base_gc, effects_gc, e.getX(), e.getY()));
  }

  @SubscribeEvent
  private void onNewWorkspace(NewFileRequest req) {
    Workspace ws = new Workspace("Workspace " + workspaces.size(), DEFAULT_WIDTH, DEFAULT_HEIGHT);
    GraphicsContext gc = ws.getBaseLayer().getGraphicsContext2D();
    gc.setFill(Color.WHITE);
    gc.fillRect(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);

    initCanvasForWorkspace(ws);
    addWorkspaceTab(ws);
  }

  @SubscribeEvent
  private void createWorkspaceFromFile(FileOpenedEvent evt) throws FileNotFoundException {
    Image img = new Image(new FileInputStream(evt.file()));
    Workspace ws = new Workspace(evt.file().getName(), img.getWidth(), img.getHeight());
    ws.getBaseLayer().getGraphicsContext2D().drawImage(img, 0, 0);
    ws.setFile(evt.file());

    initCanvasForWorkspace(ws);
    addWorkspaceTab(ws);
  }

  @SubscribeEvent
  private void onCloseWorkspace(CloseCurrentWorkspaceRequest req) {
    Workspace ws = getActiveWorkspace();
    if (ws == null) return;

    if (ws.getDirtyFlag()) {
      saveWarningService.promptWorkspaceClose(ws, () -> {
        workspaces.remove(ws);

        workspaceTabPane.getTabs().stream()
          .filter(tab -> tab.getContent() == ws.getScrollPane())
          .findFirst().ifPresent(tabToRemove -> workspaceTabPane.getTabs().remove(tabToRemove));
      });
    }
  }

  @SubscribeEvent
  private void saveWorkspace(WorkspaceSaveRequest req) {
    Workspace ws = getActiveWorkspace();

    // Try to get the file from workspace
    if (ws.getFile() != null) {
      bus.post(new WorkspaceSavedEvent(ws));
    } else {
      bus.post(new WorkspaceSavedAsEvent(ws));
    }
  }

  @SubscribeEvent
  private void saveWorkspaceAs(WorkspaceSaveAsRequest req) {
    Workspace ws = getActiveWorkspace();
    bus.post(new WorkspaceSavedAsEvent(ws));
  }

  @SubscribeEvent
  private void handlePanning(ToolChangedEvent evt) {
    for (Workspace ws : workspaces) {
      ws.getScrollPane().setPannable(evt.tool() instanceof PanTool);
    }
    this.currentTool = evt.tool();
  }

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

  public Workspace getActiveWorkspace() {
    int index = workspaceTabPane.getSelectionModel().getSelectedIndex();
    return (index >= 0 && index < workspaces.size()) ? workspaces.get(index) : null;
  }

  @SubscribeEvent
  private void onDirtyWorkspacesRequest(DirtyWorkspacesRequest req) {
    bus.post(new GetDirtyWorkspaceEvent(getDirtyWorkspaces()));
  }

  public List<Workspace> getDirtyWorkspaces() {
    return workspaces.stream().filter(Workspace::getDirtyFlag).toList();
  }

  public List<Workspace> getWorkspaces() {
    return workspaces;
  }
}