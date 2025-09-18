package net.cnoga.paint.workspace;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;

public class ZoomCapability {

  private final ScrollPane scrollPane;
  private final Group target;
  private double zoomFactor = 1.0;

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

  public void zoomIn(double mouseX, double mouseY) {
    applyZoom(1.1, mouseX, mouseY);
  }

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
