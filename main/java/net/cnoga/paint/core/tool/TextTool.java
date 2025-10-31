package net.cnoga.paint.core.tool;

import java.util.Objects;
import java.util.Optional;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Font;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.events.response.ColorChangedEvent;
import net.cnoga.paint.core.bus.events.response.WidthChangedEvent;
import net.cnoga.paint.core.tool.capabilities.ColorCapability;
import net.cnoga.paint.core.tool.capabilities.WidthCapability;

/**
 * A tool for creating text on the canvas.
 * <p>
 * The {@code TextTool} allows the user to click on the canvas to place text. The text color
 * responds to {@link ColorChangedEvent}, and the font size responds to {@link WidthChangedEvent}.
 */
@EventBusSubscriber
public class TextTool extends Tool implements WidthCapability, ColorCapability {

  /**
   * Constructs a new Text tool with its icon and help text.
   */
  public TextTool() {
    super.name = "Text";
    super.helpInfo = "[Text] Left click to start text creation.";
    super.iconPath = Objects.requireNonNull(getClass()
        .getResource("/net/cnoga/paint/icons/tools/text.png"))
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
  public void onMouseReleased(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Insert Text");
    dialog.setHeaderText("Enter text to place on canvas:");
    dialog.setContentText("Text:");

    Optional<String> result = dialog.showAndWait();
    result.ifPresent(text -> {
      gc.setFill(Tool.getCurrentColor());
      gc.setFont(new Font(Tool.getCurrentWidth() <= 12 ? 12 : Tool.getCurrentWidth()));
      gc.fillText(text, x, y);
    });
  }
}