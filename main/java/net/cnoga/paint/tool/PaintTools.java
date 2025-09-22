package net.cnoga.paint.tool;

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

  public static final Tool TOOL = new Tool(); // merely done to get it on the event bus
  public static final Tool SELECT = new SelectionTool();
  public static final Tool MOVE = new MoveTool();
  public static final Tool BRUSH = new BrushTool();
  public static final Tool LINE = new LineTool();
  public static final Tool PAN = new PanTool();
  public static final Tool DROPPER = new DropperTool();
  public static final Tool ERASER = new EraserTool();
  public static final Tool SHAPES = new ShapesTool();
  public static final Tool TEXT = new TextTool();

  static {
    ToolRegistry.register(BRUSH);
    ToolRegistry.register(LINE);
    ToolRegistry.register(SHAPES);
    ToolRegistry.register(ERASER);
    ToolRegistry.register(DROPPER);
    ToolRegistry.register(PAN);
    ToolRegistry.register(TEXT);
    ToolRegistry.register(SELECT);
    ToolRegistry.register(MOVE);
    ToolRegistry.lockRegistry();
  }

  private PaintTools() {
  } // initializes to nuffin
}
