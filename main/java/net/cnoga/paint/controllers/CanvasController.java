package net.cnoga.paint.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import net.cnoga.paint.services.CanvasListener;

public class CanvasController {
  @FXML
  public StackPane main_stackpane;

  @FXML
  public CanvasListener initCanvasService() {
    return new CanvasListener(main_stackpane);
  }
}