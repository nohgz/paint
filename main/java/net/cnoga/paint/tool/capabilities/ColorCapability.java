package net.cnoga.paint.tool.capabilities;

import net.cnoga.paint.events.request.ColorChangedEvent;

public interface ColorCapability {

  void updateColorEvent(ColorChangedEvent evt);
}
