package net.cnoga.paint.services;


import javafx.event.Event;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.ImageOpenEvent;
import net.cnoga.paint.events.FileSaveEvent;
import net.cnoga.paint.events.NewFileEvent;

@EventBusSubscriber
public class CanvasService {

  // TODO:
  // The one canvas right now is good for Friday's sprint, but I want to modify
  // this guy to be able to hold multiple images/tabs. Then maybe another class
  // that makes a canvas that holds layers. tbh that's a later problem.

  private StackPane mainStackPane;

  public CanvasService(StackPane stackPane) {
    this.mainStackPane = stackPane;
  }

  @SubscribeEvent
  private void onFileOpened(ImageOpenEvent event) {
    System.out.println(
      "CanvasService sees image: " + event.image().getWidth());
    Image img = event.image();

    // Create a new Canvas matching the image size
    Canvas canvas = new Canvas(img.getWidth(), img.getHeight());

    // Draw the image onto the Canvas
    GraphicsContext gc = canvas.getGraphicsContext2D();
    canvas.widthProperty().bind(mainStackPane.widthProperty());
    canvas.heightProperty().bind(mainStackPane.heightProperty());
    gc.drawImage(img, 0, 0);

    // Optionally clear previous children and add the new Canvas
    mainStackPane.getChildren().clear();
    mainStackPane.getChildren().add(canvas);
  }

  @SubscribeEvent
  private void newFile(NewFileEvent event) {
    System.out.println("CanvasService sees NewFileEvent!");
    mainStackPane.getChildren().add(new Canvas());
  }

  @SubscribeEvent
  private void onImageSave(FileSaveEvent event) {
    System.out.println("CanvasService sees FileSaveEvent!");
  }
}
