package net.cnoga.paint.tool;

import javafx.scene.canvas.GraphicsContext;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.WidthChangedEvent;
import net.cnoga.paint.tool.capabilities.WidthCapability;

/**
 * A tool for erasing pixels from the canvas.
 * <p>
 * The {@code EraserTool} allows the user to erase by clicking
 * or dragging across the canvas. The eraser size responds to
 * {@link WidthChangedEvent} updates.
 */
@EventBusSubscriber
public class EraserTool extends Tool implements WidthCapability {

  private double lastX, lastY;

  /**
   * Constructs a new Eraser tool with its icon and help text.
   */
  public EraserTool() {
    super.name = "Eraser";
    super.helpInfo = "[Eraser] Left click to erase pixels.";
    super.iconPath = getClass()
      .getResource("/net/cnoga/paint/icons/tools/eraser.png")
      .toExternalForm();
  }

  /**
   * Starts erasing at the clicked point.
   *
   * @param gc        the main drawing context
   * @param effectsGc the effects drawing context
   * @param x         x-coordinate of the cursor
   * @param y         y-coordinate of the cursor
   */
  @Override
  public void onMousePressed(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    lastX = x;
    lastY = y;
    eraseSquare(gc, currentWidth, x, y);
  }

  /**
   * Erases a continuous line while dragging the mouse.
   *
   * @param gc        the main drawing context
   * @param effectsGc the effects drawing context
   * @param x         x-coordinate of the cursor
   * @param y         y-coordinate of the cursor
   */
  @Override
  public void onMouseDragged(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    eraseLine(gc, currentWidth, lastX, lastY, x, y);
    lastX = x;
    lastY = y;
  }

  /**
   * Finalizes the erasing stroke when the mouse is released.
   *
   * @param gc        the main drawing context
   * @param effectsGc the effects drawing context
   * @param x         x-coordinate of the cursor
   * @param y         y-coordinate of the cursor
   */
  @Override
  public void onMouseReleased(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    eraseLine(gc, currentWidth, lastX, lastY, x, y);
  }

  /**
   * Updates the eraser width when a {@link WidthChangedEvent} is received.
   *
   * @param evt the width change event
   */
  @SubscribeEvent
  public void updateWidthEvent(WidthChangedEvent evt) {
    super.currentWidth = evt.width();
  }

  /**
   * Clears a square region centered at the given coordinates.
   *
   * @param gc      the graphics context
   * @param size    the size of the eraser
   * @param centerX the x-coordinate of the square's center
   * @param centerY the y-coordinate of the square's center
   */
  private void eraseSquare(GraphicsContext gc, double size, double centerX, double centerY) {
    double half = size / 2.0;
    gc.clearRect(centerX - half, centerY - half, size, size);
  }

  /**
   * Erases a line by repeatedly clearing small squares between two points.
   *
   * @param gc   the graphics context
   * @param size the size of the eraser
   * @param x0   starting x-coordinate
   * @param y0   starting y-coordinate
   * @param x1   ending x-coordinate
   * @param y1   ending y-coordinate
   */
  private void eraseLine(GraphicsContext gc, double size, double x0, double y0, double x1, double y1) {
    double dx = x1 - x0;
    double dy = y1 - y0;
    double distance = Math.hypot(dx, dy);
    int steps = (int) distance;

    for (int i = 0; i <= steps; i++) {
      double t = (steps == 0) ? 0.0 : (double) i / steps;
      double x = x0 + t * dx;
      double y = y0 + t * dy;
      eraseSquare(gc, size, x, y);
    }
  }
}