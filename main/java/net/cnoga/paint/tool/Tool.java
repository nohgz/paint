package net.cnoga.paint.tool;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;

/**
 * Abstract base class for all paint tools.
 *
 * <p>Defines common properties such as name, icon, current color,
 * and stroke width, as well as empty mouse event hooks that tools
 * can override to provide specific drawing behavior.</p>
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
  protected String helpInfo = "[Tool] If you are able to see this text, something is wrong.";
  protected String name = "Tool";
  protected String iconPath = getClass().getResource("/net/cnoga/paint/icons/tools/tool.png").toExternalForm();
  protected Color currentColor = Color.BLACK;
  protected Integer currentWidth = 1;

  public Tool() {
    bus.register(this);
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

  public void onMousePressed(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {

  }

  public void onMouseDragged(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {

  }

  public void onMouseReleased(GraphicsContext gc, GraphicsContext effects_gc, double x, double y) {

  }
}
