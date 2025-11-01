package net.cnoga.paint.core.tool;

import java.util.Objects;
import javafx.scene.canvas.GraphicsContext;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.events.request.MoveSelectionRequest;

/**
 * Tool for moving a selection on the canvas.
 *
 * <p>
 * The user clicks and drags to move the currently active selection. Movement deltas are
 * posted as {@link net.cnoga.paint.core.bus.events.request.MoveSelectionRequest} events on the
 * centralized event bus.
 * </p>
 */
@EventBusSubscriber
public class MoveTool extends Tool {

  /** Last recorded x position of the cursor. */
  private double lastX;

  /** Last recorded y position of the cursor. */
  private double lastY;

  /**
   * Constructs a MoveTool with its name, help text, and icon.
   */
  public MoveTool() {
    super.name = "Move";
    super.helpInfo = "[Move] Left click and drag to move the selection.";
    super.iconPath = Objects.requireNonNull(getClass()
        .getResource("/net/cnoga/paint/icons/tools/move.png"))
      .toExternalForm();
  }

  @Override
  public void onMousePressed(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    lastX = x;
    lastY = y;
  }

  @Override
  public void onMouseDragged(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    double dx = x - lastX;
    double dy = y - lastY;
    lastX = x;
    lastY = y;
    bus.post(new MoveSelectionRequest(dx, dy));
  }
}