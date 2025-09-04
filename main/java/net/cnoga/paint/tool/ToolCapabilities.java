package net.cnoga.paint.tool;

import javafx.scene.canvas.GraphicsContext;

public interface ToolCapabilities {
  void onMousePressed(GraphicsContext gc, double x, double y);

  void onMouseDragged(GraphicsContext gc, double x, double y);

  void onMouseReleased(GraphicsContext gc, double x, double y);
}
