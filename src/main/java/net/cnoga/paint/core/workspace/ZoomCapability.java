package net.cnoga.paint.core.workspace;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;

/**
 * Provides zooming functionality for a workspace.
 *
 * <p>Zooms in or out when the user scrolls while holding Ctrl. Adjusts the
 * viewport so the mouse position remains stable.</p>
 */
public class ZoomCapability {

  /** Scroll pane containing the canvas group. */
  private final ScrollPane scrollPane;

  /** Group node to scale for zooming. */
  private final Group target;

  /** Current zoom factor. */
  private double zoomFactor = 1.0;

  /**
   * Constructs a zoom capability for the given scroll pane and target group.
   *
   * @param scrollPane the scroll pane containing the canvas
   * @param target     the group to scale when zooming
   */
  public ZoomCapability(ScrollPane scrollPane, Group target) {
    this.scrollPane = scrollPane;
    this.target = target;

    scrollPane.addEventFilter(ScrollEvent.SCROLL, this::onScroll);
  }

  private void onScroll(ScrollEvent event) {
    if (!event.isControlDown()) {
      return;
    }
    if (event.getDeltaY() > 0) {
      zoomIn(event.getX(), event.getY());
    } else {
      zoomOut(event.getX(), event.getY());
    }
    event.consume();
  }

  /** Zooms in around the given mouse coordinates. */
  public void zoomIn(double mouseX, double mouseY) {
    applyZoom(1.1, mouseX, mouseY);
  }

  /** Zooms out around the given mouse coordinates. */
  public void zoomOut(double mouseX, double mouseY) {
    applyZoom(0.9, mouseX, mouseY);
  }

  private void applyZoom(double factor, double mouseX, double mouseY) {
    double oldZoom = zoomFactor;
    zoomFactor *= factor;
    zoomFactor = Math.max(0.125, Math.min(zoomFactor, 4));

    target.setScaleX(zoomFactor);
    target.setScaleY(zoomFactor);

    // keep mouse in place relative to viewport
    double adjustmentX =
      (mouseX + scrollPane.getHvalue() * scrollPane.getContent().getBoundsInLocal().getWidth())
        * (zoomFactor / oldZoom - 1);
    double adjustmentY =
      (mouseY + scrollPane.getVvalue() * scrollPane.getContent().getBoundsInLocal().getHeight())
        * (zoomFactor / oldZoom - 1);

    scrollPane.setHvalue(
      scrollPane.getHvalue() + adjustmentX / scrollPane.getContent().getBoundsInLocal().getWidth());
    scrollPane.setVvalue(
      scrollPane.getVvalue() + adjustmentY / scrollPane.getContent().getBoundsInLocal()
        .getHeight());
  }
}
