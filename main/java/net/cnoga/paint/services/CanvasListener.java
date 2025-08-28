package net.cnoga.paint.services;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.ImageOpenEvent;
import net.cnoga.paint.events.ImageSaveEvent;
import net.cnoga.paint.events.NewImageEvent;

@EventBusSubscriber
public class CanvasListener {

  // TODO:
  // The one canvas right now is good for Friday's sprint, but I want to modify
  // this guy to be able to hold multiple images/tabs. Then maybe another class
  // that makes a canvas that holds layers. tbh that's a later problem.

  private StackPane mainStackPane;

  public CanvasListener(StackPane stackPane) {
    this.mainStackPane = stackPane;
  }

  @SubscribeEvent
  private void onFileOpened(ImageOpenEvent event) {
    Image img = event.image();

    // Create a new Canvas
    Canvas canvas = new Canvas();

    // Get GraphicsContext
    GraphicsContext gc = canvas.getGraphicsContext2D();

    // Bind canvas size to StackPane
    canvas.widthProperty().bind(mainStackPane.widthProperty());
    canvas.heightProperty().bind(mainStackPane.heightProperty());

    // Redraw image whenever canvas size changes
    canvas.widthProperty().addListener((obs, oldVal, newVal) -> redraw(gc, img, canvas));
    canvas.heightProperty().addListener((obs, oldVal, newVal) -> redraw(gc, img, canvas));

    // Initial draw
    redraw(gc, img, canvas);

    // Clear previous children and add new canvas
    mainStackPane.getChildren().clear();
    mainStackPane.getChildren().add(canvas);
  }

  @SubscribeEvent
  private void newFile(NewImageEvent event) {
    System.out.println("CanvasService sees NewFileEvent!");

    // Open a dialog that asks for size of canvas

    final Canvas canvas = new Canvas(250,250);
    GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

    graphicsContext.setFill(Color.BLACK);
    graphicsContext.fillRect(0,0,250,250);

    graphicsContext.setFill(Color.BLUE);
    graphicsContext.fillRect(75,75,100,100);

    mainStackPane.getChildren().add(canvas);
  }

  @SubscribeEvent
  private void onImageSave(ImageSaveEvent event) {
    System.out.println("CanvasService sees FileSaveEvent!");
  }

  private void redraw(GraphicsContext gc, Image img, Canvas canvas) {
    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    gc.drawImage(img, 0, 0, canvas.getWidth(), canvas.getHeight());
  }

}
