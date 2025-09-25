package net.cnoga.paint.core.tool;

import javafx.scene.canvas.GraphicsContext;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.response.ShapeChangedEvent;
import net.cnoga.paint.core.tool.capabilities.ColorCapability;
import net.cnoga.paint.core.tool.capabilities.WidthCapability;
import net.cnoga.paint.core.util.ShapeConfig;
import net.cnoga.paint.core.util.ShapeType;
import net.cnoga.paint.core.util.ShapeUtil;

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
    effects_gc.setStroke(Tool.getCurrentColor());
    effects_gc.setLineWidth(Tool.getCurrentWidth());

    ShapeUtil.drawShape(effects_gc, lastX, lastY, x, y, shapeConfig);
  }

  @Override
  public void onMouseReleased(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    effects_gc.clearRect(0, 0, effects_gc.getCanvas().getWidth(),
      effects_gc.getCanvas().getHeight());
    gc.setStroke(Tool.getCurrentColor());
    gc.setLineWidth(Tool.getCurrentWidth());
    ShapeUtil.drawShape(gc, lastX, lastY, x, y, shapeConfig);
  }

  @SubscribeEvent
  public void onShapeChanged(ShapeChangedEvent evt) {
    this.shapeConfig = evt.shapeConfig();
  }

  public ShapeConfig getShapeConfig() {
    return shapeConfig;
  }
}
