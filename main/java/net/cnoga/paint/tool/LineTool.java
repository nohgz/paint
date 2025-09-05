package net.cnoga.paint.tool;

import static net.cnoga.paint.util.ShapeUtil.drawLineWithCircles;

import javafx.scene.canvas.GraphicsContext;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.ToolColorChangeRequest;
import net.cnoga.paint.events.request.ToolWidthChangeRequest;

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
