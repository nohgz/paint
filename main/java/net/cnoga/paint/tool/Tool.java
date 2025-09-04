package net.cnoga.paint.tool;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.ColorChangeEvent;

@EventBusSubscriber
public class Tool implements ToolCapabilities{
  protected String name = "Tool";
  protected String iconPath = getClass().getResource("/net/cnoga/paint/icons/tools/tool.png").toExternalForm();
  protected Color currentColor = Color.BLACK;

  @SubscribeEvent
  protected void updateBrushColor(ColorChangeEvent event) {
    this.currentColor = event.color();
  }

  public String getName() {
    return name;
  }

  public String getIconPath() {
    return iconPath;
  }

  @Override
  public void onMousePressed(GraphicsContext gc, double x, double y) {

  }

  @Override
  public void onMouseDragged(GraphicsContext gc, double x, double y) {

  }

  @Override
  public void onMouseReleased(GraphicsContext gc, double x, double y) {

  }
}
