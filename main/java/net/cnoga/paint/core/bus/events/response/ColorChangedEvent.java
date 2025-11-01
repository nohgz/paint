package net.cnoga.paint.core.bus.events.response;

import javafx.scene.paint.Color;

/** Event indicating the global color has changed.
 * @param color the new drawing {@link Color}
 */
public record ColorChangedEvent(Color color) {

}
