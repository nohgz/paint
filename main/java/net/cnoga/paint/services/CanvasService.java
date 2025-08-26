package net.cnoga.paint.services;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.events.ImageOpenEvent;
import net.cnoga.paint.events.FileSaveEvent;
import net.cnoga.paint.events.NewFileEvent;

public class CanvasService extends EventBusAware {

  // TODO:
  // The one canvas right now is good for Friday's sprint, but I want to modify
  // this guy to be able to hold multiple images/tabs. Then maybe another class
  // that makes a canvas that holds layers. That's a later problem.

  private final StackPane mainStackPane;

  public CanvasService(EventBus bus, StackPane mainStackPane) {
    super(bus);
    this.mainStackPane = mainStackPane;
    bus.subscribe(ImageOpenEvent.class, this::onFileOpened);
    bus.subscribe(FileSaveEvent.class, this::onImageSave);
    bus.subscribe(NewFileEvent.class, this::newFile);
  }

  private void onFileOpened(ImageOpenEvent event) {
    System.out.println(
      "I'M THE CANVAS SERVICE AND I SEE AN IMAGE " + event.image().getHeight() + " COMING MY WAY");

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

  private void newFile(NewFileEvent event) {
    System.out.println("NEW FILE LIL BRO!!!!");
    mainStackPane.getChildren().add(new Canvas());
  }

  private void onImageSave(FileSaveEvent event) {
    System.out.println("SAVE TWIN");
  }
}
