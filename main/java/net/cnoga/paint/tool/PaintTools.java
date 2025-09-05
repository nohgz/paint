package net.cnoga.paint.tool;


public final class PaintTools {
  public static final Tool BRUSH = new BrushTool();
  public static final Tool LINE = new LineTool();
  public static final Tool PAN = new PanTool();

  static {
    ToolRegistry.register(BRUSH);
    ToolRegistry.register(LINE);
    ToolRegistry.register(PAN);
    ToolRegistry.lockRegistry(); // freeze registration
  }

  private PaintTools() {} // utility class
}
