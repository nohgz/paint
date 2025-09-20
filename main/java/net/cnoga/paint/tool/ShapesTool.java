package net.cnoga.paint.tool;

import static net.cnoga.paint.util.ShapeUtil.drawShape;

import javafx.scene.canvas.GraphicsContext;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.ColorChangedEvent;
import net.cnoga.paint.events.request.WidthChangedEvent;
import net.cnoga.paint.events.response.ShapeChangedEvent;
import net.cnoga.paint.tool.capabilities.ColorCapability;
import net.cnoga.paint.tool.capabilities.WidthCapability;
import net.cnoga.paint.util.ShapeConfig;
import net.cnoga.paint.util.ShapeType;
import net.cnoga.paint.util.ShapeUtil;

@EventBusSubscriber
public class ShapesTool extends Tool implements WidthCapability, ColorCapability {

  private double lastX, lastY;
  private ShapeConfig shapeConfig = new ShapeConfig(ShapeType.RECTANGLE, 8, false);

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
    effects_gc.clearRect(0, 0, effects_gc.getCanvas().getWidth(),
      effects_gc.getCanvas().getHeight());

    // draw preview
    effects_gc.setStroke(currentColor);
    effects_gc.setLineWidth(currentWidth);

    ShapeUtil.drawShape(effects_gc, lastX, lastY, x, y, shapeConfig);
  }

  @Override
  public void onMouseReleased(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    effects_gc.clearRect(0, 0, effects_gc.getCanvas().getWidth(),
      effects_gc.getCanvas().getHeight());
    gc.setStroke(currentColor);
    gc.setLineWidth(currentWidth);
    ShapeUtil.drawShape(gc, lastX, lastY, x, y, shapeConfig);
  }

  @SubscribeEvent
  public void onWidthChanged(WidthChangedEvent evt) {
    super.currentWidth = evt.width();
  }

  @SubscribeEvent
  public void onColorChanged(ColorChangedEvent evt) {
    super.currentColor = evt.color();
  }

  @SubscribeEvent
  public void onShapeChanged(ShapeChangedEvent evt) {
    this.shapeConfig = evt.shapeConfig();
  }

  public ShapeConfig getShapeConfig() {
    return shapeConfig;
  }
}
