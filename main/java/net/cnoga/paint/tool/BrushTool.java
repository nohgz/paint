package net.cnoga.paint.tool;

import static net.cnoga.paint.tool.LineUtil.drawCircle;
import static net.cnoga.paint.tool.LineUtil.drawLineWithCircles;

import javafx.scene.canvas.GraphicsContext;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.ColorChangedEvent;
import net.cnoga.paint.events.request.WidthChangedEvent;
import net.cnoga.paint.tool.capabilities.ColorCapability;
import net.cnoga.paint.tool.capabilities.WidthCapability;

/**
 * A simple freehand paintbrush tool that draws continuous strokes
 * following the mouse cursor, using the currently selected color and width.
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
    super.iconPath = getClass()
      .getResource("/net/cnoga/paint/icons/tools/brush.png")
      .toExternalForm();
  }

  @Override
  public void onMousePressed(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    gc.setFill(currentColor);

    lastX = x;
    lastY = y;

    drawCircle(gc, currentWidth, x, y);
  }

  @Override
  public void onMouseDragged(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    drawLineWithCircles(gc, currentWidth, lastX, lastY, x, y);
    lastX = x;
    lastY = y;
  }

  @Override
  public void onMouseReleased(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    drawLineWithCircles(gc, currentWidth, lastX, lastY, x, y);
  }

  @SubscribeEvent
  public void updateWidthEvent(WidthChangedEvent evt) {
    super.currentWidth = evt.width();
  }

  @SubscribeEvent
  public void updateColorEvent(ColorChangedEvent evt) {
    super.currentColor = evt.color();
  }
}
