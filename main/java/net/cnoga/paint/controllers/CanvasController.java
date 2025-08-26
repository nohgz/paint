package net.cnoga.paint.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.services.CanvasService;

public class CanvasController {
  @FXML
  public StackPane main_stackpane;

  @FXML
  public CanvasService initCanvasService() {
    return new CanvasService(main_stackpane);
  }
}