package net.cnoga.paint.core.tool;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.events.request.SelectionRequest;

@EventBusSubscriber
public class SelectionTool extends Tool {

  private double startX, startY;

  public SelectionTool() {
    super.name = "Selection";
    super.helpInfo = "[Selection] Left click and drag to create a selection.";
    super.iconPath = getClass()
      .getResource("/net/cnoga/paint/icons/tools/select.png")
      .toExternalForm();
    super.isMutator = false;
  }

  @Override
  public void onMousePressed(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    startX = x;
    startY = y;
  }

  @Override
  public void onMouseDragged(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    double oldWidth = effects_gc.getLineWidth();
    Color oldColor = (Color) effects_gc.getStroke();

    effects_gc.clearRect(0, 0, effects_gc.getCanvas().getWidth(),
      effects_gc.getCanvas().getHeight());
    effects_gc.setLineDashes(5);
    effects_gc.setStroke(Color.BLACK);
    effects_gc.setLineWidth(1);
    effects_gc.strokeRect(Math.min(x, startX), Math.min(y, startY),
      Math.abs(x - startX), Math.abs(y - startY));

    // Reset state
    effects_gc.setLineDashes(0);
    effects_gc.setStroke(oldColor);
    effects_gc.setLineWidth(oldWidth);
  }

  @Override
  public void onMouseReleased(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    effects_gc.clearRect(0, 0, effects_gc.getCanvas().getWidth(),
      effects_gc.getCanvas().getHeight());

    Rectangle2D bounds = new Rectangle2D(
      Math.min(startX, x),
      Math.min(startY, y),
      Math.abs(x - startX),
      Math.abs(y - startY)
    );

    bus.post(new SelectionRequest(bounds));
  }
}