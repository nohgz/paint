package net.cnoga.paint.core.workspace;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.request.CommitSelectionRequest;
import net.cnoga.paint.core.bus.events.request.CopySelectionRequest;
import net.cnoga.paint.core.bus.events.request.MoveSelectionRequest;
import net.cnoga.paint.core.bus.events.request.SelectionRequest;
import net.cnoga.paint.core.bus.events.response.SelectionPastedEvent;
import net.cnoga.paint.core.bus.events.response.ToolChangedEvent;
import net.cnoga.paint.core.tool.MoveTool;

@EventBusSubscriber
public class SelectionCapability extends EventBusPublisher {
  private final Workspace workspace;
  private WritableImage buffer;
  private Rectangle2D selectionBounds;
  private double offsetX, offsetY;
  private boolean copyMode = false;

  public SelectionCapability(Workspace workspace) {
    this.workspace = workspace;
    bus.register(this);
  }

  @SubscribeEvent
  private void onSelectionRequest(SelectionRequest req) {
    selectionBounds = req.bounds();
    if (selectionBounds.getWidth() <= 0 || selectionBounds.getHeight() <= 0) {
      return;
    }

    SnapshotParameters params = new SnapshotParameters();
    params.setViewport(selectionBounds);
    params.setFill(Color.TRANSPARENT);

    buffer = new WritableImage((int) selectionBounds.getWidth(), (int) selectionBounds.getHeight());
    workspace.getBaseLayer().snapshot(params, buffer);

    offsetX = offsetY = 0;
    copyMode = false; // selection means we are cutting by default

    previewSelection(selectionBounds.getMinX(), selectionBounds.getMinY());
  }

  @SubscribeEvent
  private void onMoveSelection(MoveSelectionRequest req) {
    if (buffer == null) {
      return;
    }

    // Only clear the original area if this is NOT a copy
    if (!copyMode) {
      workspace.getBaseLayer()
        .getGraphicsContext2D()
        .clearRect(selectionBounds.getMinX(), selectionBounds.getMinY(),
          selectionBounds.getWidth(), selectionBounds.getHeight());
    }

    offsetX += req.dx();
    offsetY += req.dy();

    double drawX = selectionBounds.getMinX() + offsetX;
    double drawY = selectionBounds.getMinY() + offsetY;

    previewSelection(drawX, drawY);
  }

  @SubscribeEvent
  private void onToolChanged(ToolChangedEvent evt) {
    if (!(evt.tool() instanceof MoveTool)) {
      bus.post(new CommitSelectionRequest());
    }
  }

  @SubscribeEvent
  private void onCommitSelection(CommitSelectionRequest req) {
    if (buffer == null || selectionBounds == null) {
      return;
    }

    GraphicsContext base = workspace.getBaseLayer().getGraphicsContext2D();
    base.drawImage(buffer, selectionBounds.getMinX() + offsetX,
      selectionBounds.getMinY() + offsetY);

    clearEffects();
    buffer = null;
    selectionBounds = null;
    offsetX = offsetY = 0;
    copyMode = false;
  }

  private void previewSelection(double x, double y) {
    GraphicsContext effects = workspace.getEffectsLayer().getGraphicsContext2D();
    clearEffects();

    effects.drawImage(buffer, x, y);

    double oldWidth = effects.getLineWidth();
    Color oldColor = (Color) effects.getStroke();

    effects.setLineWidth(1.0);
    effects.setStroke(Color.BLUE);
    effects.setLineDashes(4);
    effects.strokeRect(x, y, buffer.getWidth(), buffer.getHeight());

    effects.setLineDashes(0);
    effects.setStroke(oldColor);
    effects.setLineWidth(oldWidth);
  }

  private void clearEffects() {
    GraphicsContext effects = workspace.getEffectsLayer().getGraphicsContext2D();
    effects.clearRect(0, 0, effects.getCanvas().getWidth(), effects.getCanvas().getHeight());
  }

  @SubscribeEvent
  private void onCopySelection(CopySelectionRequest req) {
    if (buffer == null) {
      return;
    }

    ClipboardContent content = new ClipboardContent();
    content.putImage(buffer);
    Clipboard.getSystemClipboard().setContent(content);
  }

  @SubscribeEvent
  private void onPasteSelection(SelectionPastedEvent evt) {
    Clipboard clipboard = Clipboard.getSystemClipboard();
    if (!clipboard.hasImage()) {
      return;
    }

    Image img = clipboard.getImage();

    buffer = new WritableImage(img.getPixelReader(),
      (int) img.getWidth(), (int) img.getHeight());
    selectionBounds = new Rectangle2D(evt.x(), evt.y(), img.getWidth(), img.getHeight());

    offsetX = offsetY = 0;
    copyMode = true; // pasted = copy mode

    previewSelection(selectionBounds.getMinX(), selectionBounds.getMinY());
  }

  public boolean hasSelection() {
    return buffer != null && selectionBounds != null;
  }
}