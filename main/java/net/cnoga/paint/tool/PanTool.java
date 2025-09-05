package net.cnoga.paint.tool;

import net.cnoga.paint.bus.EventBusSubscriber;

@EventBusSubscriber
public class PanTool extends Tool {
  public PanTool() {
    super.name = "Pan";
    super.iconPath = getClass()
      .getResource("/net/cnoga/paint/icons/tools/pan.png")
      .toExternalForm();
  }
}
