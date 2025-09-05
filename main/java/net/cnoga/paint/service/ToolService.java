package net.cnoga.paint.service;

import javafx.scene.paint.Color;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.events.response.ColorChangedEvent;
import net.cnoga.paint.events.response.ToolChangedEvent;
import net.cnoga.paint.tool.Tool;

public class ToolService extends EventBusPublisher {

  public void selectTool(Tool tool) {
    bus.post(new ToolChangedEvent(tool));
  }
  public void requestColorChange(Color color) {
    bus.post(new ColorChangedEvent(color));
  }
}
