package net.cnoga.paint.core.bus.events.response;

import net.cnoga.paint.core.tool.Tool;

/** Event indicating the active tool has changed.
 * @param tool the new {@link Tool}
 */
public record ToolChangedEvent(Tool tool) {

}
