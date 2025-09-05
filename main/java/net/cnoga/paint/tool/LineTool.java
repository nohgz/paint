package net.cnoga.paint.tool;

import static net.cnoga.paint.util.ShapeUtil.drawLineWithCircles;

import javafx.scene.canvas.GraphicsContext;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.ToolColorChangeRequest;
import net.cnoga.paint.events.request.ToolWidthChangeRequest;

/**
 * A tool for drawing straight lines between two points.
 *
 * <p>The user clicks to define the starting point and releases the mouse
 * to define the ending point. The line is drawn with the currently
 * selected color and width.</p>
 *
 * <p>Listens to {@link ToolColorChangeRequest} and {@link ToolWidthChangeRequest}
 * events on the event bus to update its color and stroke width dynamically.</p>
 */
@EventBusSubscriber
public class LineTool extends Tool {

  private double startX, startY;

  public LineTool() {
    super.name = "Line";
    super.iconPath = getClass()
      .getResource("/net/cnoga/paint/icons/tools/line.png")
      .toExternalForm();
  }

  @Override
  public void onMousePressed(GraphicsContext gc, double x, double y) {
    gc.setFill(currentColor);
    startX = x;
    startY = y;
  }

  @Override
  public void onMouseDragged(GraphicsContext gc, double x, double y) {
    // maybe something that shows a preview of the line?
  }

  @Override
  public void onMouseReleased(GraphicsContext gc, double x, double y) {
    drawLineWithCircles(gc, currentWidth, startX, startY, x, y);
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
