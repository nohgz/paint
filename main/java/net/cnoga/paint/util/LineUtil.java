package net.cnoga.paint.util;

import javafx.scene.canvas.GraphicsContext;


/**
 * Utility class for line drawing operations.
 *
 * <p>Provides helper methods to render smooth lines and circles
 * using a {@link GraphicsContext}. The line drawing uses circle stamping to ensure consistent
 * thickness across different directions.</p>
 */
public final class LineUtil {

  private LineUtil() {
    // Prevent instantiation
  }

  /**
   * Caps off lines with a circle.
   *
   * @param gc           the {@link GraphicsContext} to draw on
   * @param currentWidth the diameter of the circle (usually stroke width)
   * @param x            the x-coordinate of the circle’s center
   * @param y            the y-coordinate of the circle’s center
   */
  public static void roundLineCap(GraphicsContext gc, double currentWidth, double x, double y) {
    double r = currentWidth / 2;
    gc.fillOval(x - r, y - r, currentWidth, currentWidth);
  }

  /**
   * Draws a line between two points by stamping circles along the path. This technique creates
   * smoother and more consistent line thickness compared to relying on the graphics context’s
   * built-in stroke line.
   *
   * @param gc           the {@link GraphicsContext} to draw on
   * @param currentWidth the thickness of the line
   * @param x0           the starting x-coordinate
   * @param y0           the starting y-coordinate
   * @param x1           the ending x-coordinate
   * @param y1           the ending y-coordinate
   */
  public static void drawLineWithCircles(GraphicsContext gc, double currentWidth,
    double x0, double y0, double x1, double y1) {
    double dx = x1 - x0;
    double dy = y1 - y0;
    double distance = Math.hypot(dx, dy);
    int steps = (int) distance;

    for (int i = 0; i <= steps; i++) {
      double t = (steps == 0) ? 0.0 : (double) i / steps;
      double x = x0 + t * dx;
      double y = y0 + t * dy;
      roundLineCap(gc, currentWidth, x, y);
    }
  }
}
