package net.cnoga.paint.tool;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Central registry for managing all available tools in the application.
 *
 * <p>Tools must be registered at application startup, after which the
 * registry can be locked to prevent accidental modification. Once locked, no new tools can be
 * added.</p>
 *
 * <p>Provides access to individual tools by name or all registered tools
 * as an unmodifiable map.</p>
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Registering tools before the registry is locked.</li>
 *   <li>Preventing duplicate or late registrations.</li>
 *   <li>Providing lookup methods for tools by name.</li>
 *   <li>Exposing the complete tool collection for iteration.</li>
 * </ul>
 */
public final class ToolRegistry {

  private static final Map<String, Tool> tools = new HashMap<>();
  private static boolean locked = false;

  private ToolRegistry() {
  } // No instances

  // Register a tool; must be done at startup
  public static void register(Tool tool) {
    if (locked) {
      throw new IllegalStateException("Cannot register tools after registry is locked.");
    }
    if (tools.containsKey(tool.getName())) {
      throw new IllegalArgumentException("Tool already registered: " + tool.getName());
    }
    tools.put(tool.getName(), tool);
  }

  // Lock registry after initialization
  public static void lockRegistry() {
    locked = true;
  }

  public static Tool get(String name) {
    Tool tool = tools.get(name);
    if (tool == null) {
      throw new IllegalArgumentException("Tool not found: " + name);
    }
    return tool;
  }

  public static Map<String, Tool> getAll() {
    return Collections.unmodifiableMap(tools);
  }
}
