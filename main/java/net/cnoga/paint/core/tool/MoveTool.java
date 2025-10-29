package net.cnoga.paint.core.tool;

import java.util.Objects;
import javafx.scene.canvas.GraphicsContext;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.init.InitWorkspaceBrewRequest;
import net.cnoga.paint.core.bus.events.request.MoveSelectionRequest;

@EventBusSubscriber
public class MoveTool extends Tool {

  private double lastX, lastY;

  public MoveTool() {
    super.name = "Move";
    super.helpInfo = "[Move] Left click and drag to move the selection.";
    super.iconPath = Objects.requireNonNull(getClass()
        .getResource("/net/cnoga/paint/icons/tools/move.png"))
      .toExternalForm();
  }

  @SubscribeEvent
  private void onInitWorkspaceBrew(InitWorkspaceBrewRequest req) {

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

  @Override
  public void onMouseReleased(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {
    // do nothing?
  }
}