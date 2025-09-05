package net.cnoga.paint.tool;

import static net.cnoga.paint.util.ShapeUtil.drawCircle;
import static net.cnoga.paint.util.ShapeUtil.drawLineWithCircles;

import javafx.scene.canvas.GraphicsContext;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.ToolColorChangeRequest;
import net.cnoga.paint.events.request.ToolWidthChangeRequest;

/**
 * A simple freehand paintbrush tool that draws continuous strokes
 * in the current color.
 */
@EventBusSubscriber
public class BrushTool extends Tool {

  private double lastX, lastY;

  public BrushTool() {
    super.name = "Paintbrush";
    super.iconPath = getClass()
      .getResource("/net/cnoga/paint/icons/tools/brush.png")
      .toExternalForm();
  }

  @Override
  public void onMousePressed(GraphicsContext gc, double x, double y) {
    gc.setFill(currentColor);

    lastX = x;
    lastY = y;

    drawCircle(gc, currentWidth, x, y);
  }

  @Override
  public void onMouseDragged(GraphicsContext gc, double x, double y) {
    drawLineWithCircles(gc, currentWidth, lastX, lastY, x, y);
    lastX = x;
    lastY = y;
  }

  @Override
  public void onMouseReleased(GraphicsContext gc, double x, double y) {
    drawLineWithCircles(gc, currentWidth, lastX, lastY, x, y);
  }

  @SubscribeEvent
  protected void updateColorEvent(ToolColorChangeRequest req) {
    super.currentColor = req.color();
  }

  @SubscribeEvent
  protected void updateWidthEvent(ToolWidthChangeRequest req) {
    super.currentWidth = req.width();
  }
}
