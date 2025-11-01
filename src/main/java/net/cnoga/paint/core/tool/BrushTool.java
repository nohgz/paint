package net.cnoga.paint.core.tool;

import static net.cnoga.paint.core.util.LineUtil.drawLineWithCircles;
import static net.cnoga.paint.core.util.LineUtil.roundLineCap;

import java.util.Objects;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.events.response.ColorChangedEvent;
import net.cnoga.paint.core.bus.events.response.WidthChangedEvent;

/**
 * A simple freehand paintbrush tool that draws continuous strokes following the mouse cursor, using
 * the currently selected color and width.
 *
 * <p>Brush strokes are rendered as connected circles to create smooth lines.</p>
 *
 * <p>Listens to {@link ColorChangedEvent} and {@link WidthChangedEvent}
 * events on the event bus to update its color and stroke width dynamically.</p>
 */
@EventBusSubscriber
public class BrushTool extends Tool implements ColorCapability, WidthCapability {

  private double lastX, lastY;

  public BrushTool() {
    super.name = "Paintbrush";
    super.helpInfo = "[Paintbrush] Left click to draw with the selected color.";
    super.iconPath = Objects.requireNonNull(getClass()
        .getResource("/net/cnoga/paint/icons/tools/brush.png"))
      .toExternalForm();
  }

  @Override
  public void onMousePressed(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    lastX = x;
    lastY = y;

    effects_gc.setFill(Tool.getCurrentColor());
    roundLineCap(effects_gc, Tool.getCurrentWidth(), x, y);
  }

  @Override
  public void onMouseDragged(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    drawLineWithCircles(effects_gc, Tool.getCurrentWidth(), lastX, lastY, x, y);
    lastX = x;
    lastY = y;
  }

  @Override
  public void onMouseReleased(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    // Finish the stroke on effects layer
    drawLineWithCircles(effects_gc, Tool.getCurrentWidth(), lastX, lastY, x, y);

    // Prepare a transparent snapshot
    SnapshotParameters params = new SnapshotParameters();
    params.setFill(Color.TRANSPARENT);  // Preserve transparency

    WritableImage snapshot = new WritableImage(
      (int) effects_gc.getCanvas().getWidth(),
      (int) effects_gc.getCanvas().getHeight()
    );

    effects_gc.getCanvas().snapshot(params, snapshot);

    // Draw onto base without destroying underlying pixels
    gc.drawImage(snapshot, 0, 0);

    // Clear effects for next stroke
    effects_gc.clearRect(0, 0, effects_gc.getCanvas().getWidth(),
      effects_gc.getCanvas().getHeight());
  }
}
