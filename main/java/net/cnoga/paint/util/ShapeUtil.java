package net.cnoga.paint.util;

import javafx.scene.canvas.GraphicsContext;

/**
 * Utility class providing common drawing operations for shapes.
 *
 * <p>Currently includes helpers for rendering circles and smooth lines
 * composed of connected circles, which are used by tools like the brush
 * and line tool to achieve anti-aliased strokes.</p>
 *
 * <p>All methods are static and stateless.</p>
 */
public final class ShapeUtil {

  public static void drawCircle(GraphicsContext gc, double currentWidth, double x, double y) {
    double r = currentWidth / 2;
    gc.fillOval(x - r, y - r, currentWidth, currentWidth);
  }

  public static void drawLineWithCircles(GraphicsContext gc, double currentWidth, double x0,
    double y0, double x1, double y1) {
    double dx = x1 - x0;
    double dy = y1 - y0;
    double distance = Math.hypot(dx, dy);
    int steps = (int) distance;

    for (int i = 0; i <= steps; i++) {
      double t = (steps == 0) ? 0.0 : (double) i / steps;
      double x = x0 + t * dx;
      double y = y0 + t * dy;
      drawCircle(gc, currentWidth, x, y);
    }
  }
}
