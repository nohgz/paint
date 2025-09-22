package net.cnoga.paint.workspace;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.CommitSelectionRequest;
import net.cnoga.paint.events.request.MoveSelectionRequest;
import net.cnoga.paint.events.request.PasteSelectionRequest;
import net.cnoga.paint.events.request.SelectionRequest;

@EventBusSubscriber
public class SelectionCapability extends EventBusPublisher  {

  private final Workspace workspace;
  private WritableImage buffer;
  private Rectangle2D selectionBounds;

  public SelectionCapability(Workspace workspace) {
    this.workspace = workspace;
    bus.register(this);
  }

  @SubscribeEvent
  public void onSelectionRequest(SelectionRequest req) {
    Rectangle2D bounds = req.bounds();
    if (bounds.getWidth() <= 0 || bounds.getHeight() <= 0) return;

    Canvas baseLayer = workspace.getBaseLayer();
    WritableImage snapshot = new WritableImage((int) baseLayer.getWidth(), (int) baseLayer.getHeight());

    // Take full snapshot of the base canvas
    baseLayer.snapshot(null, snapshot);

    // Crop only the selection bounds
    buffer = new WritableImage(
      snapshot.getPixelReader(),
      (int) bounds.getMinX(),
      (int) bounds.getMinY(),
      (int) bounds.getWidth(),
      (int) bounds.getHeight()
    );

    selectionBounds = bounds;

    // Clear original area
    GraphicsContext base = baseLayer.getGraphicsContext2D();
    base.clearRect(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
  }

  @SubscribeEvent
  public void onMoveSelection(MoveSelectionRequest req) {
    if (buffer == null || selectionBounds == null) return;
    selectionBounds = new Rectangle2D(
      selectionBounds.getMinX() + req.dx(),
      selectionBounds.getMinY() + req.dy(),
      selectionBounds.getWidth(),
      selectionBounds.getHeight()
    );
  }

  @SubscribeEvent
  public void onPasteSelection(PasteSelectionRequest req) {
    if (buffer == null) return;
    selectionBounds = new Rectangle2D(
      req.x(),
      req.y(),
      buffer.getWidth(),
      buffer.getHeight()
    );
  }

  public void commitSelection() {
    if (buffer == null || selectionBounds == null) return;

    GraphicsContext base = workspace.getBaseLayer().getGraphicsContext2D();
    base.drawImage(buffer, selectionBounds.getMinX(), selectionBounds.getMinY());
    buffer = null;
    selectionBounds = null;
  }

  public void draw(GraphicsContext gc) {
    if (selectionBounds != null) {
      gc.setLineDashes(5);
      gc.setStroke(Color.BLACK);
      gc.strokeRect(selectionBounds.getMinX(), selectionBounds.getMinY(),
        selectionBounds.getWidth(), selectionBounds.getHeight());
      gc.setLineDashes(0);

      if (buffer != null) {
        gc.drawImage(buffer, selectionBounds.getMinX(), selectionBounds.getMinY());
      }
    }
  }

  @SubscribeEvent
  private void onCommitSelection(CommitSelectionRequest req) {
    commitSelection();
  }

  // Copy to system clipboard
  public void copyToClipboard() {
    if (buffer == null) return;
    Clipboard clipboard = Clipboard.getSystemClipboard();
    ClipboardContent content = new ClipboardContent();
    content.putImage(buffer);
    clipboard.setContent(content);
  }

  // Paste from system clipboard
  public void pasteFromClipboard() {
    Clipboard clipboard = Clipboard.getSystemClipboard();
    if (clipboard.hasImage()) {
      buffer = (WritableImage) clipboard.getImage();
      selectionBounds = new Rectangle2D(0, 0, buffer.getWidth(), buffer.getHeight());
    }
  }

  public boolean hasSelection() {
    return buffer != null && selectionBounds != null;
  }
}
