package net.cnoga.paint.tool;

import net.cnoga.paint.tool.capabilities.ShapesTool;

/**
 * Utility class providing access to all built-in paint tools.
 *
 * <p>This class registers and exposes the application's core tools,
 * including {@link BrushTool}, {@link LineTool}, and {@link PanTool}. After registration, the
 * {@link ToolRegistry} is locked to prevent further modifications.</p>
 *
 * <p>Designed as a static holder class with no instances.</p>
 */
public final class PaintTools {

  public static final Tool BRUSH = new BrushTool();
  public static final Tool LINE = new LineTool();
  public static final Tool PAN = new PanTool();
  public static final Tool DROPPER = new DropperTool();
  public static final Tool ERASER = new EraserTool();
  public static final Tool SHAPES = new ShapesTool();

  static {
    ToolRegistry.register(BRUSH);
    ToolRegistry.register(LINE);
    ToolRegistry.register(SHAPES);
    ToolRegistry.register(ERASER);
    ToolRegistry.register(DROPPER);
    ToolRegistry.register(PAN);
    ToolRegistry.lockRegistry();
  }

  private PaintTools() {
  } // initializes to nuffin
}
