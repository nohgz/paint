package net.cnoga.paint.events.init;

import javafx.scene.control.ToggleButton;

public record InitSubWindowServiceRequest(
  ToggleButton historyButton,
  ToggleButton toolsButton,
  ToggleButton layersButton,
  ToggleButton settingsButton) {

}
