package net.cnoga.paint.tool;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;

@EventBusSubscriber
public class Tool extends EventBusPublisher {
  protected String name = "Tool";
  protected String iconPath = getClass().getResource("/net/cnoga/paint/icons/tools/tool.png").toExternalForm();
  protected Color currentColor = Color.BLACK;
  protected Integer currentWidth = 1;

  public Tool() {
    bus.register(this);
  }

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
