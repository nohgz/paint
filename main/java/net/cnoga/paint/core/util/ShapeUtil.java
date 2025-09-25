package net.cnoga.paint.core.util;

import javafx.scene.canvas.GraphicsContext;

/**
 * Utility class for shape drawing operations.
 *
 * <p>Provides helper methods for drawing rectangles, ellipses, polygons,
 * and other shapes onto a {@link GraphicsContext}. Coordinates are interpreted as opposite corners
 * of a bounding box.</p>
 */
public final class ShapeUtil {

  private ShapeUtil() {
    // Prevent instantiation
  }

  /**
   * Draws a shape defined by two bounding coordinates and a ShapeConfig.
   *
   * @param gc     the {@link GraphicsContext} to draw on
   * @param x0     the first x-coordinate (corner of bounding box)
   * @param y0     the first y-coordinate (corner of bounding box)
   * @param x1     the second x-coordinate (opposite corner of bounding box)
   * @param y1     the second y-coordinate (opposite corner of bounding box)
   * @param config the {@link ShapeConfig} describing type and properties
   */
  public static void drawShape(GraphicsContext gc, double x0, double y0,
    double x1, double y1, ShapeConfig config) {

    double left = Math.min(x0, x1);
    double top = Math.min(y0, y1);
    double width = Math.abs(x1 - x0);
    double height = Math.abs(y1 - y0);

    switch (config.type()) {
      case RECTANGLE -> gc.strokeRect(left, top, width, height);
      case SQUARE -> {
        double side = Math.max(width, height);
        gc.strokeRect(left, top, side, side);
      }
      case ELLIPSE -> gc.strokeOval(left, top, width, height);
      case CIRCLE -> {
        double diameter = Math.max(width, height);
        gc.strokeOval(left, top, diameter, diameter);
      }
      case TRIANGLE -> {
        if (config.isRightTriangle()) {
          gc.strokePolygon(
            new double[]{left, left, left + width},
            new double[]{top, top + height, top + height},
            3
          );
        } else {
          double midX = left + width / 2;
          gc.strokePolygon(
            new double[]{midX, left, left + width},
            new double[]{top, top + height, top + height},
            3
          );
        }
      }
      case N_GON -> drawRegularPolygon(gc, x0, y0, x1, y1, config.sides());
      case DONUT -> drawDonut(gc, x0, y0, x1, y1);
    }
  }

  public static void drawRegularPolygon(GraphicsContext gc,
    double x0, double y0, double x1, double y1,
    int sides) {
    double centerX = (x0 + x1) / 2;
    double centerY = (y0 + y1) / 2;
    double radius = Math.min(Math.abs(x1 - x0), Math.abs(y1 - y0)) / 2;

    double[] xs = new double[sides];
    double[] ys = new double[sides];

    for (int i = 0; i < sides; i++) {
      double angle = 2 * Math.PI * i / sides - Math.PI / 2; // rotate to point up
      xs[i] = centerX + radius * Math.cos(angle);
      ys[i] = centerY + radius * Math.sin(angle);
    }

    gc.strokePolygon(xs, ys, sides);
  }


  /**
   * Draws a donut: two concentric circles (outer + inner "hole").
   */
  public static void drawDonut(GraphicsContext gc,
    double x0, double y0, double x1, double y1) {

    double left = Math.min(x0, x1);
    double top = Math.min(y0, y1);
    double width = Math.abs(x1 - x0);
    double height = Math.abs(y1 - y0);

    // Outer circle
    double outerDiameter = Math.max(width, height);
    double centerX = left + width / 2;
    double centerY = top + height / 2;

    gc.strokeOval(centerX - outerDiameter / 2, centerY - outerDiameter / 2,
      outerDiameter, outerDiameter);

    // Inner circle (half radius of outer)
    double innerDiameter = outerDiameter / 2;
    gc.strokeOval(centerX - innerDiameter / 2, centerY - innerDiameter / 2,
      innerDiameter, innerDiameter);
  }
}