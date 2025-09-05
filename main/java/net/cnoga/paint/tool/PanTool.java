package net.cnoga.paint.tool;

import net.cnoga.paint.bus.EventBusSubscriber;

/**
 * A simple pan tool used for moving the canvas view.
 */
@EventBusSubscriber
public class PanTool extends Tool {
  public PanTool() {
    super.name = "Pan";
    super.iconPath = getClass()
      .getResource("/net/cnoga/paint/icons/tools/pan.png")
      .toExternalForm();
  }
}
