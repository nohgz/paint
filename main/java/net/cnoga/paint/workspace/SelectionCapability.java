package net.cnoga.paint.workspace;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
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
import net.cnoga.paint.events.response.ToolChangedEvent;
import net.cnoga.paint.tool.MoveTool;

@EventBusSubscriber
public class SelectionCapability extends EventBusPublisher  {

  private final Workspace workspace;
  private WritableImage buffer;
  private Rectangle2D selectionBounds;
  private double offsetX, offsetY;

  public SelectionCapability(Workspace workspace) {
    this.workspace = workspace;
    bus.register(this);
  }


  @SubscribeEvent
  public void onSelectionRequest(SelectionRequest req) {
    Rectangle2D bounds = req.bounds();
    if (bounds.getWidth() <= 0 || bounds.getHeight() <= 0) return;

    // Snapshot the selected area into buffer
    SnapshotParameters params = new SnapshotParameters();
    params.setViewport(bounds);

    buffer = new WritableImage((int) bounds.getWidth(), (int) bounds.getHeight());
    workspace.getBaseLayer().snapshot(params, buffer);

    selectionBounds = bounds;

    // Clear original area
    workspace.getBaseLayer()
      .getGraphicsContext2D()
      .clearRect(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());

    // Draw preview immediately
    previewSelection(bounds.getMinX(), bounds.getMinY());
  }

  @SubscribeEvent
  public void onMoveSelection(MoveSelectionRequest req) {
    if (buffer == null) return;

    offsetX += req.dx();
    offsetY += req.dy();

    double drawX = selectionBounds.getMinX() + offsetX;
    double drawY = selectionBounds.getMinY() + offsetY;

    previewSelection(drawX, drawY);
  }

  @SubscribeEvent
  private void onPasteSelection(PasteSelectionRequest req) {
    if (buffer == null) return;
    selectionBounds = new Rectangle2D(
      req.x(),
      req.y(),
      buffer.getWidth(),
      buffer.getHeight()
    );
  }

  @SubscribeEvent
  private void onToolChanged(ToolChangedEvent evt) {
    if (!(evt.tool() instanceof MoveTool)) {
      bus.post(new CommitSelectionRequest());
    }
  }

  @SubscribeEvent
  private void onCommitSelection(CommitSelectionRequest req) {
    if (buffer == null || selectionBounds == null) return;

    // Draw permanently onto base layer
    GraphicsContext base = workspace.getBaseLayer().getGraphicsContext2D();
    base.drawImage(buffer, selectionBounds.getMinX() + offsetX, selectionBounds.getMinY() + offsetY);

    // Clear the preview
    clearEffects();
    buffer = null;
    selectionBounds = null;
    offsetX = offsetY = 0;
  }

  private void previewSelection(double x, double y) {
    GraphicsContext effects = workspace.getEffectsLayer().getGraphicsContext2D();
    clearEffects();
    effects.drawImage(buffer, x, y);
    // optional: add border for clarity
    effects.setStroke(Color.BLUE);
    effects.setLineDashes(4);
    effects.strokeRect(x, y, buffer.getWidth(), buffer.getHeight());
    effects.setLineDashes(0);
  }

  private void clearEffects() {
    GraphicsContext effects = workspace.getEffectsLayer().getGraphicsContext2D();
    effects.clearRect(0, 0, effects.getCanvas().getWidth(), effects.getCanvas().getHeight());
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
