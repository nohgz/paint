package net.cnoga.paint.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import net.cnoga.paint.util.ImageHelper;

public class CanvasController {

  @FXML private StackPane main_canvas;

  public void addImage(String filePath) {
    WritableImage writableImage = ImageHelper.loadWritableImage(filePath);
    main_canvas.getChildren().add(new ImageView(writableImage));
  }
}
