package net.cnoga.paint.tool;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.ToolColorChangedRequest;
import net.cnoga.paint.events.request.ToolWidthChangeRequest;

public class Tool {
  protected String name = "Tool";
  protected String iconPath = getClass().getResource("/net/cnoga/paint/icons/tools/tool.png").toExternalForm();
  protected Color currentColor = Color.BLACK;

  public String getName() {
    return name;
  }

  public String getIconPath() {
    return iconPath;
  }

  public void onMousePressed(GraphicsContext gc, double x, double y) {

  }

  public void onMouseDragged(GraphicsContext gc, double x, double y) {

  }

  public void onMouseReleased(GraphicsContext gc, double x, double y) {

  }
}
