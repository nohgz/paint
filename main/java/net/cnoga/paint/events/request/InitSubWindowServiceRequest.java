package net.cnoga.paint.events.request;

import javafx.scene.control.ToggleButton;

public record InitSubWindowServiceRequest(
    ToggleButton historyButton,
    ToggleButton toolsButton,
    ToggleButton layersButton,
    ToggleButton colorPickerButton,
    ToggleButton settingsButton) { }
