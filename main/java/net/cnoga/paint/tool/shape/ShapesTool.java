package net.cnoga.paint.tool.shape;

import javafx.scene.canvas.GraphicsContext;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.ColorChangedEvent;
import net.cnoga.paint.events.request.WidthChangedEvent;
import net.cnoga.paint.events.response.ShapeChangedEvent;
import net.cnoga.paint.tool.Tool;
import net.cnoga.paint.tool.capabilities.ColorCapability;
import net.cnoga.paint.tool.capabilities.WidthCapability;


@EventBusSubscriber
public class ShapesTool extends Tool implements WidthCapability, ColorCapability {

  private double lastX, lastY;
  private ShapeType currentShape = ShapeType.RECTANGLE;
  private int polygonSides = 5;

  public ShapesTool() {
    super.name = "Shape";
    super.helpInfo = "[Shape] Left click and drag to draw the selected shape.";
    super.iconPath = getClass()
      .getResource("/net/cnoga/paint/icons/tools/shapes.png")
      .toExternalForm();
  }

  @Override
  public void onMousePressed(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    lastX = x;
    lastY = y;
  }

  @Override
  public void onMouseDragged(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    // clear previous preview
    effects_gc.clearRect(0, 0, effects_gc.getCanvas().getWidth(), effects_gc.getCanvas().getHeight());

    // draw preview
    effects_gc.setStroke(currentColor);
    effects_gc.setLineWidth(currentWidth);
    effects_gc.setLineDashes(25);

    drawShape(effects_gc, lastX, lastY, x, y, true);

    effects_gc.setLineDashes(0);
  }

  @Override
  public void onMouseReleased(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    effects_gc.clearRect(0, 0, effects_gc.getCanvas().getWidth(), effects_gc.getCanvas().getHeight());
    gc.setStroke(currentColor);
    gc.setLineWidth(currentWidth);
    drawShape(gc, lastX, lastY, x, y, false);
  }

  @SubscribeEvent
  public void updateWidthEvent(WidthChangedEvent evt) {
    super.currentWidth = evt.width();
  }

  @SubscribeEvent
  public void updateColorEvent(ColorChangedEvent evt) {
    super.currentColor = evt.color();
  }

  @SubscribeEvent
  public void updateShapeEvent(ShapeChangedEvent evt) {
    this.currentShape = evt.shapeType();
    this.polygonSides = evt.sides();
  }

  private void drawShape(GraphicsContext gc, double x0, double y0, double x1, double y1, boolean preview) {
    double left = Math.min(x0, x1);
    double top = Math.min(y0, y1);
    double width = Math.abs(x1 - x0);
    double height = Math.abs(y1 - y0);

    switch (currentShape) {
      case RECTANGLE:
        gc.strokeRect(left, top, width, height);
        break;
      case SQUARE:
        double side = Math.max(width, height);
        gc.strokeRect(left, top, side, side);
        break;
      case ELLIPSE:
        gc.strokeOval(left, top, width, height);
        break;
      case CIRCLE:
        double diameter = Math.max(width, height);
        gc.strokeOval(left, top, diameter, diameter);
        break;
      case TRIANGLE:
        double midX = left + width / 2;
        gc.strokePolygon(
          new double[]{midX, left, left + width},
          new double[]{top, top + height, top + height},
          3
        );
        break;
      case POLYGON:
        drawRegularPolygon(gc, x0, y0, x1, y1, polygonSides);
        break;
    }
  }

  private void drawRegularPolygon(GraphicsContext gc, double x0, double y0, double x1, double y1, int sides) {
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
  public ShapeType getCurrentShape() { return currentShape; }
  public int getPolygonSides() { return polygonSides; }
}
