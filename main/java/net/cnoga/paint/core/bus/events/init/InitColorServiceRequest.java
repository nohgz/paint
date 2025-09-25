package net.cnoga.paint.core.bus.events.init;

import javafx.scene.control.ColorPicker;

public record InitColorServiceRequest(ColorPicker colorPicker) {

}
