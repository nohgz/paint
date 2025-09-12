package net.cnoga.paint.tool;

import static net.cnoga.paint.util.LineUtil.drawLineWithCircles;

import javafx.scene.canvas.GraphicsContext;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.ColorChangedEvent;
import net.cnoga.paint.events.request.WidthChangedEvent;
import net.cnoga.paint.tool.capabilities.ColorCapability;
import net.cnoga.paint.tool.capabilities.WidthCapability;

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

  private double startX, startY;

  public LineTool() {
    super.name = "Line";
    super.helpInfo = "[Line] Click and hold to draw a line.";
    super.iconPath = getClass()
      .getResource("/net/cnoga/paint/icons/tools/line.png")
      .toExternalForm();
  }

  @Override
  public void onMousePressed(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    gc.setFill(currentColor);
    startX = x;
    startY = y;
  }

  @Override
  public void onMouseDragged(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    // Clear previous preview
    effects_gc.clearRect(0, 0, effects_gc.getCanvas().getWidth(),
      effects_gc.getCanvas().getHeight());

    // Setup preview style
    effects_gc.setStroke(currentColor);
    effects_gc.setLineWidth(currentWidth);
    effects_gc.setLineDashes(25);

    // Draw preview line
    effects_gc.strokeLine(startX, startY, x, y);

    // Reset dashes so it doesn’t affect later drawings
    effects_gc.setLineDashes(0);
  }

  @Override
  public void onMouseReleased(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    // Clear preview
    effects_gc.clearRect(0, 0, effects_gc.getCanvas().getWidth(),
      effects_gc.getCanvas().getHeight());

    // Draw the final line on the main canvas
    drawLineWithCircles(gc, currentWidth, startX, startY, x, y);
  }


  @SubscribeEvent
  public void updateColorEvent(ColorChangedEvent req) {
    super.currentColor = req.color();
  }

  @SubscribeEvent
  public void updateWidthEvent(WidthChangedEvent req) {
    super.currentWidth = req.width();
  }
}
