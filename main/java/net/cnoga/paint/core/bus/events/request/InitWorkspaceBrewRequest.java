package net.cnoga.paint.core.bus.events.request;

import javafx.scene.control.TabPane;

/** Initializes the workspace manager with the main tab pane.
 * @param tabPane the main workspace {@link TabPane}
 */
public record InitWorkspaceBrewRequest(TabPane tabPane) {

}
