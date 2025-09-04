package net.cnoga.paint.tool;

import java.util.ArrayList;
import java.util.List;

public class ToolRegistry {

  private static final List<Tool> tools = new ArrayList<>();


  public static void register(Tool tool) {
    tools.add(tool);
  }

  public static List<Tool> getTools() {
    return List.copyOf(tools);
  }

  public static Tool getToolByName(String name) {
    return tools.stream()
      .filter(t -> t.getName().equals(name))
      .findFirst()
      .orElse(null);
  }
}
