package net.cnoga.paint.core.tool;

import net.cnoga.paint.core.bus.EventBusSubscriber;

/**
 * A simple pan tool used for moving the canvas view.
 */
@EventBusSubscriber
public class PanTool extends Tool {

  public PanTool() {
    super.name = "Pan";
    super.helpInfo = "[Pan] Left click and drag to navigate around the canvas";
    super.iconPath = getClass()
      .getResource("/net/cnoga/paint/icons/tools/pan.png")
      .toExternalForm();
    super.isMutator = false;
  }
}
