package net.cnoga.paint.tool;

import javafx.scene.canvas.GraphicsContext;

public class LineTool extends Tool {

  private double startX, startY;

  public LineTool() {
    super.name = "Line";
    super.iconPath = getClass()
      .getResource("/net/cnoga/paint/icons/tools/line.png")
      .toExternalForm();
  }

  @Override
  public void onMousePressed(GraphicsContext gc, double x, double y) {
    startX = x;
    startY = y;
  }

  @Override
  public void onMouseDragged(GraphicsContext gc, double x, double y) {
    // maybe something that shows a preview of the line?
  }

  @Override
  public void onMouseReleased(GraphicsContext gc, double x, double y) {
    gc.strokeLine(startX, startY, x, y);
  }
}
