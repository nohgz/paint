package net.cnoga.paint.core.tool;

import java.util.Objects;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.request.SaveStateRequest;
import net.cnoga.paint.core.bus.events.response.ColorChangedEvent;
import net.cnoga.paint.core.bus.events.response.WidthChangedEvent;

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

  /** Global drawing color shared across all tools. */
  private static Color currentColor = Color.BLACK;

  /** Global stroke width shared across all tools. */
  private static Integer currentWidth = 1;

  /** Human-readable description of the tool’s purpose. */
  protected String helpInfo =
    "[Tool] If you are able to see this text, something is wrong.";

  /** The tool’s display name. */
  protected String name = "Tool";

  /** If the tool actually makes changes. */
  protected Boolean isMutator = true;

  /** Path to the tool’s icon resource. */
  protected String iconPath =
    Objects.requireNonNull(getClass().getResource("/net/cnoga/paint/icons/tools/tool.png")).toExternalForm();

  /** Constructs a new {@code Tool} and registers it on the global event bus. */
  public Tool() {
    bus.register(this);
  }

  /**
   * Returns the current global drawing color.
   *
   * @return the current {@link Color} used for drawing
   */
  public static Color getCurrentColor() {
    return currentColor;
  }

  /**
   * Returns the current global stroke width.
   *
   * @return the current stroke width in pixels
   */
  public static Integer getCurrentWidth() {
    return currentWidth;
  }

  /**
   * Handles color change events by updating the global drawing color.
   *
   * @param evt the {@link ColorChangedEvent} containing the new color
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onColorChanged(ColorChangedEvent evt) {
    currentColor = evt.color();
  }


  /**
   * Handles stroke width change events by updating the global width.
   *
   * @param evt the {@link WidthChangedEvent} containing the new width
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onWidthChanged(WidthChangedEvent evt) {
    currentWidth = evt.width();
  }

  /**
   * Returns the tool’s display name.
   *
   * @return the name of the tool
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the path to the tool’s icon resource.
   *
   * @return the icon path as a {@link String}
   */
  public String getIconPath() {
    return iconPath;
  }

  /**
   * Returns help text describing the tool’s function.
   *
   * @return a human-readable description of the tool
   */
  public String getHelpInfo() {
    return helpInfo;
  }

  /**
   * Tools hook into this
   *
   * @param gc the main {@link GraphicsContext} for drawing
   * @param effectsGc the overlay {@link GraphicsContext} for preview or effects
   * @param x the x-coordinate of the mouse press
   * @param y the y-coordinate of the mouse press
   */
  protected void onMousePressed(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    // no-op by default
  }

  /**
   * Tools hook into this
   *
   * @param gc the main {@link GraphicsContext} for drawing
   * @param effectsGc the overlay {@link GraphicsContext} for preview or effects
   * @param x the x-coordinate of the mouse drag
   * @param y the y-coordinate of the mouse drag
   */
  protected void onMouseDragged(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    // no-op by default
  }

  /**
   * Tools hook into this
   *
   * @param gc the main {@link GraphicsContext} for drawing
   * @param effectsGc the overlay {@link GraphicsContext} for preview or effects
   * @param x the x-coordinate of the mouse release
   * @param y the y-coordinate of the mouse release
   */
  protected void onMouseReleased(GraphicsContext gc, GraphicsContext effectsGc, double x,
    double y) {
    // no-op by default
  }

  /**
   * The other stuff hooks into this
   *
   * @param gc the main {@link GraphicsContext} for drawing
   * @param effectsGc the overlay {@link GraphicsContext} for preview or effects
   * @param x the x-coordinate of the mouse press
   * @param y the y-coordinate of the mouse press
   */
  public void handleMousePressed(GraphicsContext gc, GraphicsContext effectsGc, double x,
    double y) {
    onMousePressed(gc, effectsGc, x, y);
    if (isMutator) {
      bus.post(new SaveStateRequest());
    }
  }

  /**
   * The other stuff hooks into this
   *
   * @param gc the main {@link GraphicsContext} for drawing
   * @param effectsGc the overlay {@link GraphicsContext} for preview or effects
   * @param x the x-coordinate of the mouse drag
   * @param y the y-coordinate of the mouse drag
   */
  public void handleMouseDragged(GraphicsContext gc, GraphicsContext effectsGc, double x,
    double y) {
    onMouseDragged(gc, effectsGc, x, y);
  }

  /**
   * The other stuff hooks into this
   *
   * @param gc the main {@link GraphicsContext} for drawing
   * @param effectsGc the overlay {@link GraphicsContext} for preview or effects
   * @param x the x-coordinate of the mouse release
   * @param y the y-coordinate of the mouse release
   */
  public void handleMouseReleased(GraphicsContext gc, GraphicsContext effectsGc, double x,
    double y) {
    onMouseReleased(gc, effectsGc, x, y);
  }
}