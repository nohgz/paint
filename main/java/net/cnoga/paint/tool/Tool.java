package net.cnoga.paint.tool;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;

/**
 * Abstract base class for all paint tools.
 *
 * <p>Defines common properties such as name, icon, current color,
 * and stroke width, as well as empty mouse event hooks that tools can override to provide specific
 * drawing behavior.</p>
 *
 * <p>Each tool is also an event bus subscriber and may react to
 * global tool configuration changes.</p>
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Providing metadata such as name and icon.</li>
 *   <li>Exposing overridable methods for mouse interactions
 *       ({@link #onMousePressed}, {@link #onMouseDragged}, {@link #onMouseReleased}).</li>
 *   <li>Maintaining and updating the active color and width.</li>
 * </ul>
 */
@EventBusSubscriber
public class Tool extends EventBusPublisher {

  /**
   * Human-readable description of the tool’s purpose.
   */
  protected String helpInfo =
    "[Tool] If you are able to see this text, something is wrong.";

  /**
   * The tool’s display name.
   */
  protected String name = "Tool";

  /**
   * Path to the tool’s icon resource.
   */
  protected String iconPath =
    getClass().getResource("/net/cnoga/paint/icons/tools/tool.png").toExternalForm();

  /**
   * Current drawing color for this tool.
   */
  protected Color currentColor = Color.BLACK;

  /**
   * Current stroke width for this tool.
   */
  protected Integer currentWidth = 1;

  /**
   * Constructs a new {@code Tool} and registers it on the global event bus.
   * <p>
   * Subclasses should call this constructor via {@code super()}.
   */
  public Tool() {
    bus.register(this);
  }

  /**
   * Returns the name of this tool.
   *
   * @return the tool’s name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the path to this tool’s icon resource.
   *
   * @return the tool’s icon path
   */
  public String getIconPath() {
    return iconPath;
  }

  /**
   * Returns a short help string describing how to use this tool.
   *
   * @return the help information string
   */
  public String getHelpInfo() {
    return helpInfo;
  }

  /**
   * Called when the mouse is pressed.
   * <p>
   * Default implementation does nothing; subclasses may override.
   *
   * @param gc        the main drawing context
   * @param effectsGc the effects drawing context
   * @param x         x-coordinate of the press
   * @param y         y-coordinate of the press
   */
  public void onMousePressed(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    // no-op by default
  }

  /**
   * Called when the mouse is dragged.
   * <p>
   * Default implementation does nothing; subclasses may override.
   *
   * @param gc        the main drawing context
   * @param effectsGc the effects drawing context
   * @param x         current x-coordinate of the cursor
   * @param y         current y-coordinate of the cursor
   */
  public void onMouseDragged(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    // no-op by default
  }

  /**
   * Called when the mouse is released.
   * <p>
   * Default implementation does nothing; subclasses may override.
   *
   * @param gc        the main drawing context
   * @param effectsGc the effects drawing context
   * @param x         x-coordinate of release
   * @param y         y-coordinate of release
   */
  public void onMouseReleased(GraphicsContext gc, GraphicsContext effectsGc, double x, double y) {
    // no-op by default
  }
}
