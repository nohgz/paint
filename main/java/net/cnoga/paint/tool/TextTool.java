package net.cnoga.paint.tool;

import java.util.Optional;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.ColorChangedEvent;
import net.cnoga.paint.events.request.WidthChangedEvent;
import net.cnoga.paint.tool.capabilities.ColorCapability;
import net.cnoga.paint.tool.capabilities.WidthCapability;

/**
 * A tool for creating text on the canvas.
 * <p>
 * The {@code TextTool} allows the user to click on the canvas to place text.
 * The text color responds to {@link ColorChangedEvent}, and the font size responds
 * to {@link WidthChangedEvent}.
 */
@EventBusSubscriber
public class TextTool extends Tool implements WidthCapability, ColorCapability {

  private double lastX, lastY;

  /**
   * Constructs a new Text tool with its icon and help text.
   */
  public TextTool() {
    super.name = "Text";
    super.helpInfo = "[Text] Left click to start text creation.";
    super.iconPath = getClass()
      .getResource("/net/cnoga/paint/icons/tools/text.png")
      .toExternalForm();
  }

  /**
   * Starts text creation at the clicked point.
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

    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Insert Text");
    dialog.setHeaderText("Enter text to place on canvas:");
    dialog.setContentText("Text:");

    Optional<String> result = dialog.showAndWait();
    result.ifPresent(text -> {
      gc.setFill(currentColor);
      gc.setFont(new Font(currentWidth <= 12 ? 12 : currentWidth));
      gc.fillText(text, lastX, lastY);
    });
  }

  @Override
  public void onMouseDragged(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    // Intentionally Empty
  }

  @Override
  public void onMouseReleased(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    // Intentionally Empty
  }

  /**
   * Updates the font size when a {@link WidthChangedEvent} is received.
   *
   * @param evt the width change event
   */
  @SubscribeEvent
  public void onWidthChanged(WidthChangedEvent evt) {
    super.currentWidth = evt.width();
  }

  /**
   * Updates the current color when a {@link ColorChangedEvent} is received.
   *
   * @param evt the color change event
   */
  @SubscribeEvent
  public void onColorChanged(ColorChangedEvent evt) {
    super.currentColor = evt.color();
  }
}