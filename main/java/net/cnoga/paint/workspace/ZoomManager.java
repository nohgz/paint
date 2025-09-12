package net.cnoga.paint.workspace;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;

public class ZoomManager {
  private final ScrollPane scrollPane;
  private final Group target;
  private double zoomFactor = 1.0;

  public ZoomManager(ScrollPane scrollPane, Group target) {
    this.scrollPane = scrollPane;
    this.target = target;

    scrollPane.addEventFilter(ScrollEvent.SCROLL, this::onScroll);
  }

  private void onScroll(ScrollEvent event) {
    if (!event.isControlDown()) return;

    double oldZoom = zoomFactor;
    zoomFactor *= (event.getDeltaY() > 0) ? 1.1 : 0.9;
    zoomFactor = Math.max(0.125, Math.min(zoomFactor, 4));

    target.setScaleX(zoomFactor);
    target.setScaleY(zoomFactor);

    // adjust viewport to keep mouse in place
    double mouseX = event.getX();
    double mouseY = event.getY();
    double adjustmentX = (mouseX + scrollPane.getHvalue() * scrollPane.getContent().getBoundsInLocal().getWidth())
      * (zoomFactor / oldZoom - 1);
    double adjustmentY = (mouseY + scrollPane.getVvalue() * scrollPane.getContent().getBoundsInLocal().getHeight())
      * (zoomFactor / oldZoom - 1);

    scrollPane.setHvalue(scrollPane.getHvalue() + adjustmentX / scrollPane.getContent().getBoundsInLocal().getWidth());
    scrollPane.setVvalue(scrollPane.getVvalue() + adjustmentY / scrollPane.getContent().getBoundsInLocal().getHeight());

    event.consume();
  }
}
