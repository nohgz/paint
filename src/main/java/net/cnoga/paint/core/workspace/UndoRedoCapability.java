package net.cnoga.paint.core.workspace;

import java.util.Stack;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.request.RedoRequest;
import net.cnoga.paint.core.bus.events.request.SaveStateRequest;
import net.cnoga.paint.core.bus.events.request.UndoRequest;

/**
 * Manages undo/redo functionality for a workspace.
 *
 * <p>Captures snapshots of the base layer and maintains undo/redo stacks.
 * Listens to {@link SaveStateRequest}, {@link UndoRequest}, and {@link RedoRequest} events.</p>
 */
@EventBusSubscriber
public class UndoRedoCapability extends EventBusPublisher {

  /** Undo stack of canvas snapshots. */
  private final Stack<WritableImage> undoStack = new Stack<>();

  /** Redo stack of canvas snapshots. */
  private final Stack<WritableImage> redoStack = new Stack<>();

  /** The workspace this capability belongs to. */
  private final Workspace workspace;

  /**
   * Constructs undo/redo capability for the given workspace.
   *
   * @param workspace workspace to manage undo/redo for
   */
  public UndoRedoCapability(Workspace workspace) {
    this.workspace = workspace;
    bus.register(this);
  }

  /** Saves the current workspace state to the undo stack. */
  @SubscribeEvent
  @SuppressWarnings("unused")
  public void saveState(SaveStateRequest req) {
    WritableImage snapshot = snapshotCanvas();
    undoStack.push(snapshot);
    redoStack.clear();
  }

  /** Restores the previous state from the undo stack. */
  @SubscribeEvent
  @SuppressWarnings("unused")
  public void undo(UndoRequest req) {
    if (!undoStack.isEmpty()) {
      WritableImage current = snapshotCanvas();
      redoStack.push(current);

      WritableImage prev = undoStack.pop();
      applyToCanvas(workspace.getBaseLayer(), prev);
    }
  }

  /** Restores the next state from the redo stack. */
  @SubscribeEvent
  @SuppressWarnings("unused")
  public void redo(RedoRequest req) {
    if (!redoStack.isEmpty()) {
      WritableImage current = snapshotCanvas();
      undoStack.push(current);

      WritableImage next = redoStack.pop();
      applyToCanvas(workspace.getBaseLayer(), next);
    }
  }

  /**
   * Convenience method to get snapshot the canvas for the undo/redo stack.
   *
   * @return the canvas snapshot
   */
  private WritableImage snapshotCanvas() {
    WritableImage snapshot = new WritableImage((int) workspace.getBaseLayer().getWidth(),
      (int) workspace.getBaseLayer().getHeight());

    SnapshotParameters params = new SnapshotParameters();
    params.setFill(Color.TRANSPARENT);

    workspace.getBaseLayer().snapshot(params, snapshot);
    return snapshot;
  }

  /** Draws the image from the stack onto the canvas. Used for redo-ing.
   * @param canvas the canvas to draw to
   * @param img to image in the redo stack to draw
   */
  private void applyToCanvas(Canvas canvas, WritableImage img) {
    var gc = canvas.getGraphicsContext2D();
    gc.setImageSmoothing(false);
    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    gc.drawImage(img, 0, 0);
  }
}