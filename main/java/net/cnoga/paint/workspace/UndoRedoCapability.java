package net.cnoga.paint.workspace;

import java.util.Stack;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.RedoRequest;
import net.cnoga.paint.events.request.SaveStateRequest;
import net.cnoga.paint.events.request.UndoRequest;

@EventBusSubscriber
public class UndoRedoCapability extends EventBusPublisher {

  private final Stack<WritableImage> undoStack = new Stack<>();
  private final Stack<WritableImage> redoStack = new Stack<>();
  private final Workspace workspace;

  public UndoRedoCapability(Workspace workspace) {
    this.workspace = workspace;
    bus.register(this);
  }

  @SubscribeEvent
  public void saveState(SaveStateRequest req) {
    WritableImage snapshot = snapshotCanvas();
    undoStack.push(snapshot);
    redoStack.clear();
  }

  @SubscribeEvent
  public void undo(UndoRequest req) {
    if (!undoStack.isEmpty()) {
      WritableImage current = snapshotCanvas();
      redoStack.push(current);

      WritableImage prev = undoStack.pop();
      applyToCanvas(workspace.getBaseLayer(), prev);
    }
  }

  @SubscribeEvent
  public void redo(RedoRequest req) {
    if (!redoStack.isEmpty()) {
      WritableImage current = snapshotCanvas();
      undoStack.push(current);

      WritableImage next = redoStack.pop();
      applyToCanvas(workspace.getBaseLayer(), next);
    }
  }

  private WritableImage snapshotCanvas() {
    WritableImage snapshot = new WritableImage((int) workspace.getBaseLayer().getWidth(),
      (int) workspace.getBaseLayer().getHeight());

    SnapshotParameters params = new SnapshotParameters();
    params.setFill(Color.TRANSPARENT);

    workspace.getBaseLayer().snapshot(params, snapshot);
    return snapshot;
  }

  private void applyToCanvas(Canvas canvas, WritableImage img) {
    var gc = canvas.getGraphicsContext2D();
    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    gc.drawImage(img, 0, 0);
  }
}