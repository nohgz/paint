package net.cnoga.paint.tool;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.events.request.ColorChangedEvent;

@EventBusSubscriber
public class DropperTool extends Tool {

  public DropperTool() {
    super.name = "Dropper";
    super.helpInfo = "[Dropper] Left click to pick a color on the canvas.";
    super.iconPath = getClass()
      .getResource("/net/cnoga/paint/icons/tools/dropper.png")
      .toExternalForm();
  }

  @Override
  public void onMousePressed(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    pickColor(gc, x, y);
  }

  @Override
  public void onMouseReleased(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    pickColor(gc, x, y);
  }

  private void pickColor(GraphicsContext gc, double x, double y) {
    WritableImage snapshot = gc.getCanvas().snapshot(null, null);
    PixelReader reader = snapshot.getPixelReader();
    if (reader != null &&
      x >= 0 && x < snapshot.getWidth() &&
      y >= 0 && y < snapshot.getHeight()) {
      bus.post(new ColorChangedEvent(reader.getColor((int)x, (int)y)));
    }
  }
}