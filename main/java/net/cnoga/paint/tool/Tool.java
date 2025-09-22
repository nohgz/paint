package net.cnoga.paint.tool;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.ColorChangedEvent;
import net.cnoga.paint.events.request.SaveStateRequest;
import net.cnoga.paint.events.request.WidthChangedEvent;

/**
 * Abstract base class for all paint tools.
 *
 * <p>Defines common properties such as name, icon, global color,
 * and stroke width, as well as empty mouse event hooks that tools can override.</p>
 *
 * <p>Color and width are global across all tools. When changed, all
 * tools see the new values immediately.</p>
 */
@EventBusSubscriber
public class Tool extends EventBusPublisher {

  /** Human-readable description of the tool’s purpose. */
  protected String helpInfo =
    "[Tool] If you are able to see this text, something is wrong.";

  /** The tool’s display name. */
  protected String name = "Tool";

  /** If the tool actually makes changes. */
  protected Boolean isMutator = true;

  /** Path to the tool’s icon resource. */
  protected String iconPath =
    getClass().getResource("/net/cnoga/paint/icons/tools/tool.png").toExternalForm();

  /** Global drawing color shared across all tools. */
  private static Color currentColor = Color.BLACK;

  /** Global stroke width shared across all tools. */
  private static Integer currentWidth = 1;

  /**
   * Constructs a new {@code Tool} and registers it on the global event bus.
   */
  public Tool() {
    bus.register(this);
  }

  @SubscribeEvent
  private void onColorChanged(ColorChangedEvent evt) {
    currentColor = evt.color();
  }

  @SubscribeEvent
  private void onWidthChanged(WidthChangedEvent evt) {
    currentWidth = evt.width();
  }

  public String getName() {
    return name;
  }

  public String getIconPath() {
    return iconPath;
  }

  public String getHelpInfo() {
    return helpInfo;
  }

  public static Color getCurrentColor() {
    return currentColor;
  }

  public static Integer getCurrentWidth() {
    return currentWidth;
  }

  /** Tools hook into this */
  protected void onMousePressed(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    // no-op by default
  }

  /** Tools hook into this */
  protected void onMouseDragged(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    // no-op by default
  }

  /** Tools hook into this */
  protected void onMouseReleased(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    // no-op by default
  }

  //** The other stuff hooks into this */
  public void handleMousePressed(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    onMousePressed(gc, effectsGc, x, y);
    if (isMutator) {
      bus.post(new SaveStateRequest());
    }
  }

  //** The other stuff hooks into this */
  public void handleMouseDragged(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    onMouseDragged(gc, effectsGc, x, y);
  }

  //** The other stuff hooks into this */
  public void handleMouseReleased(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    onMouseReleased(gc, effectsGc, x, y);
  }
}