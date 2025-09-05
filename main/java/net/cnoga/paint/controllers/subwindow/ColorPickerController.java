package net.cnoga.paint.controllers.subwindow;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.events.request.ToolColorChangedRequest;


public class ColorPickerController extends EventBusPublisher {
  @FXML
  private BorderPane borderPane;

  private final ColorPicker colorPicker = new ColorPicker();

  @FXML
  private void initialize() {
    borderPane.setCenter(colorPicker);

    colorPicker.setOnAction(e -> {
      Color color = colorPicker.getValue();
      System.out.println("Picked: " + color);

      bus.post(new ToolColorChangedRequest(color));
    });
  }
}
