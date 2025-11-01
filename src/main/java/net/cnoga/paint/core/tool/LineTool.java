package net.cnoga.paint.core.tool;

import static net.cnoga.paint.core.util.LineUtil.drawLineWithCircles;

import java.util.Objects;
import javafx.scene.canvas.GraphicsContext;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.events.response.ColorChangedEvent;
import net.cnoga.paint.core.bus.events.response.WidthChangedEvent;

/**
 * A tool for drawing straight lines between two points.
 *
 * <p>The user clicks to define the starting point and releases the mouse
 * to define the ending point. The line is drawn with the currently selected color and width.</p>
 *
 * <p>Listens to {@link ColorChangedEvent} and {@link WidthChangedEvent}
 * events on the event bus to update its color and stroke width dynamically.</p>
 */
@EventBusSubscriber
public class LineTool extends Tool implements ColorCapability, WidthCapability {

  /** Starting x-coordinate of the line while drawing. */
  private double startX;
  /** Starting y-coordinate of the line while drawing. */
  private double startY;

  /**
   * Creates a new LineTool and initializes its name, help text, and icon.
   */
  public LineTool() {
    super.name = "Line";
    super.helpInfo = "[Line] Click and hold to draw a line.";
    super.iconPath = Objects.requireNonNull(getClass()
        .getResource("/net/cnoga/paint/icons/tools/line.png"))
      .toExternalForm();
  }

  @Override
  public void onMousePressed(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    gc.setFill(Tool.getCurrentColor());
    startX = x;
    startY = y;
  }

  @Override
  public void onMouseDragged(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    // Clear previous preview
    effects_gc.clearRect(0, 0, effects_gc.getCanvas().getWidth(),
      effects_gc.getCanvas().getHeight());

    // Setup preview style
    effects_gc.setStroke(Tool.getCurrentColor());
    effects_gc.setLineWidth(Tool.getCurrentWidth());
    effects_gc.strokeLine(startX, startY, x, y);
  }

  @Override
  public void onMouseReleased(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    // Clear preview
    effects_gc.clearRect(0, 0, effects_gc.getCanvas().getWidth(),
      effects_gc.getCanvas().getHeight());

    drawLineWithCircles(gc, Tool.getCurrentWidth(), startX, startY, x, y);
  }
}
