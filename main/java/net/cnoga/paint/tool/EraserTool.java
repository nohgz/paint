package net.cnoga.paint.tool;

import javafx.scene.canvas.GraphicsContext;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.WidthChangedEvent;
import net.cnoga.paint.tool.capabilities.WidthCapability;

@EventBusSubscriber
public class EraserTool extends Tool implements WidthCapability {

  private double lastX, lastY;

  public EraserTool() {
    super.name = "Eraser";
    super.helpInfo = "[Eraser] Left click to erase pixels.";
    super.iconPath = getClass()
      .getResource("/net/cnoga/paint/icons/tools/eraser.png")
      .toExternalForm();
  }

  @Override
  public void onMousePressed(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    lastX = x;
    lastY = y;
    eraseSquare(gc, currentWidth, x, y);
  }

  @Override
  public void onMouseDragged(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    eraseLine(gc, currentWidth, lastX, lastY, x, y);
    lastX = x;
    lastY = y;
  }

  @Override
  public void onMouseReleased(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    eraseLine(gc, currentWidth, lastX, lastY, x, y);
  }

  @SubscribeEvent
  public void updateWidthEvent(WidthChangedEvent evt) {
    super.currentWidth = evt.width();
  }

  private void eraseSquare(GraphicsContext gc, double size, double centerX, double centerY) {
    double half = size / 2.0;
    gc.clearRect(centerX - half, centerY - half, size, size);
  }

  private void eraseLine(GraphicsContext gc, double size, double x0, double y0, double x1, double y1) {
    double dx = x1 - x0;
    double dy = y1 - y0;
    double distance = Math.hypot(dx, dy);
    int steps = (int) distance;

    for (int i = 0; i <= steps; i++) {
      double t = (steps == 0) ? 0.0 : (double) i / steps;
      double x = x0 + t * dx;
      double y = y0 + t * dy;
      eraseSquare(gc, size, x, y);
    }
  }
}