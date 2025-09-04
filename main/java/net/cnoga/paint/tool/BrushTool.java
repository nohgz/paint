package net.cnoga.paint.tool;

import javafx.scene.canvas.GraphicsContext;

/**
 * A simple freehand paintbrush tool that draws continuous strokes
 * in the current color.
 */
public class BrushTool extends Tool implements ToolCapabilities {

  public BrushTool() {
    super.name = "Paintbrush";
    super.iconPath = getClass()
      .getResource("/net/cnoga/paint/icons/tools/brush.png")
      .toExternalForm();
  }

  @Override
  public void onMousePressed(GraphicsContext gc, double x, double y) {
    gc.setStroke(currentColor);
    gc.beginPath();
    gc.moveTo(x, y);
    gc.stroke();
  }

  @Override
  public void onMouseDragged(GraphicsContext gc, double x, double y) {
    gc.lineTo(x, y);
    gc.stroke();
  }

  @Override
  public void onMouseReleased(GraphicsContext gc, double x, double y) {
    gc.lineTo(x, y);
    gc.stroke();
    gc.closePath();
  }
}
