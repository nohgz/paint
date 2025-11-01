package net.cnoga.paint.core.bus.events.request;

import javafx.scene.control.ToggleButton;

/** Initializes the subwindow service with top bar buttons.
 * @param toolsButton the tools toggle button
 * @param settingsButton the settings toggle button
 */
public record InitSubWindowServiceRequest(ToggleButton toolsButton, ToggleButton settingsButton) {

}
