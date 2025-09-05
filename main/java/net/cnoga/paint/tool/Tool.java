package net.cnoga.paint.tool;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.ToolColorChangedRequest;
import net.cnoga.paint.events.request.ToolWidthChangeRequest;

@EventBusSubscriber
public class Tool implements ToolCapabilities{
  protected String name = "Tool";
  protected String iconPath = getClass().getResource("/net/cnoga/paint/icons/tools/tool.png").toExternalForm();
  protected Integer lineWidth = 1;
  protected Color currentColor = Color.BLACK;

  @SubscribeEvent
  protected void updateToolColor(ToolColorChangedRequest req) {
    this.currentColor = req.color();
  }

  @SubscribeEvent
  protected void updateToolWidth(ToolWidthChangeRequest req) {
    this.lineWidth = req.width();
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
