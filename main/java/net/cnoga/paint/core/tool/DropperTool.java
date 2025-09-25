package net.cnoga.paint.core.tool;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.events.response.ColorChangedEvent;

/**
 * A tool for selecting colors from the canvas.
 * <p>
 * The {@code DropperTool} allows the user to pick a color by clicking on any pixel of the canvas.
 * The selected color is broadcasted via a {@link ColorChangedEvent} on the event bus.
 */
@EventBusSubscriber
public class DropperTool extends Tool {

  /**
   * Constructs a new Dropper tool with its icon and help text.
   */
  public DropperTool() {
    super.name = "Dropper";
    super.helpInfo = "[Dropper] Left click to pick a color on the canvas.";
    super.iconPath = getClass()
      .getResource("/net/cnoga/paint/icons/tools/dropper.png")
      .toExternalForm();
  }

  /**
   * Invoked when the mouse is pressed. Picks the color under the cursor immediately.
   *
   * @param gc        the main drawing context
   * @param effectsGc the effects drawing context
   * @param x         x-coordinate of the cursor
   * @param y         y-coordinate of the cursor
   */
  @Override
  public void onMousePressed(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    pickColor(gc, x, y);
  }

  /**
   * Invoked when the mouse is released. Picks the color under the cursor again for confirmation.
   *
   * @param gc        the main drawing context
   * @param effectsGc the effects drawing context
   * @param x         x-coordinate of the cursor
   * @param y         y-coordinate of the cursor
   */
  @Override
  public void onMouseReleased(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    pickColor(gc, x, y);
  }

  /**
   * Captures the canvas at the given coordinates and posts a
   * {@link net.cnoga.paint.events.request.ColorChangedEvent} with the detected color.
   *
   * @param gc the graphics context whose canvas is sampled
   * @param x  x-coordinate of the pixel
   * @param y  y-coordinate of the pixel
   */
  private void pickColor(GraphicsContext gc, double x, double y) {
    WritableImage snapshot = gc.getCanvas().snapshot(null, null);
    PixelReader reader = snapshot.getPixelReader();
    if (reader != null &&
      x >= 0 && x < snapshot.getWidth() &&
      y >= 0 && y < snapshot.getHeight()) {
      bus.post(new ColorChangedEvent(reader.getColor((int) x, (int) y)));
    }
  }
}
