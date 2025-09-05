package net.cnoga.paint.tool;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ToolRegistry {

  private static final Map<String, Tool> tools = new HashMap<>();

  public static void register(Tool tool) {
    tools.put(tool.getName(), tool);
  }

  public static Tool get(String name) {
    return tools.get(name);
  }

  public static Map<String, Tool> getAll() {
    return Collections.unmodifiableMap(tools);
  }
}
