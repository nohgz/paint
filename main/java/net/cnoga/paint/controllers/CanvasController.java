package net.cnoga.paint.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.services.CanvasService;

public class CanvasController extends AbstractIOProvider {
  @FXML
  public StackPane main_stackpane;

  public void initCanvasService(EventBus bus) {
    new CanvasService(bus, main_stackpane);
  }
}