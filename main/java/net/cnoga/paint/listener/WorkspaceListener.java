package net.cnoga.paint.listener;

import static net.cnoga.paint.util.PaintUtil.saveCanvasToFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.function.Consumer;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.FileOpenEvent;
import net.cnoga.paint.events.FileSaveAsEvent;
import net.cnoga.paint.events.FileSaveEvent;
import net.cnoga.paint.events.NewFileEvent;

@EventBusSubscriber
public class WorkspaceListener {

  private ScrollPane scrollPane;
  private StackPane stackPane;
  private Group canvasGroup;
  private Canvas canvas;
  private File currentFile;

  public WorkspaceListener(ScrollPane scrollPane, StackPane stackPane, Group canvasGroup) {
    this.scrollPane = scrollPane;
    this.stackPane = stackPane;
    this.canvasGroup = canvasGroup;
  }

  @SubscribeEvent
  private void onFileOpened(FileOpenEvent event) throws FileNotFoundException {
    currentFile = event.file();
    Image img = new Image(new FileInputStream(currentFile));
    loadCanvas(img.getWidth(), img.getHeight(), gc -> gc.drawImage(img, 0, 0));
  }

  @SubscribeEvent
  private void newFile(NewFileEvent event) {
    loadCanvas(250, 250, gc -> {
      gc.setFill(Color.BLACK);
      gc.fillRect(0, 0, 250, 250);
      gc.setFill(Color.BLUE);
      gc.fillRect(75, 75, 100, 100);
    });
  }

  private void loadCanvas(double width, double height, Consumer<GraphicsContext> drawer) {
    // Create canvas
    canvas = new Canvas(width, height);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.setImageSmoothing(false);
    drawer.accept(gc);

    // Add canvas to Group
    canvasGroup.getChildren().clear();
    canvasGroup.getChildren().add(canvas);

    // Set StackPane size to match image
    stackPane.setPrefWidth(width);
    stackPane.setPrefHeight(height);

    scrollPane.layout();
  }

  @SubscribeEvent
  private void onImageSave(FileSaveEvent event) {
    if (canvas == null) return;

    if (currentFile != null) {
      saveCanvasToFile(canvas, currentFile);
    } else {
      System.out.println("ah shit !!!");
      saveCanvasAs();
    }
  }
  @SubscribeEvent
  private void onImageSaveAs(FileSaveAsEvent event) {
    saveCanvasAs();
  }


  private void saveCanvasAs() {
    if (canvas == null) return;

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Image");
    fileChooser.getExtensionFilters().add(
      new FileChooser.ExtensionFilter("PNG Image", "*.png")
    );
    File file = fileChooser.showSaveDialog(scrollPane.getScene().getWindow());
    if (file != null) {
      saveCanvasToFile(canvas, file);
      currentFile = file;
    }
  }
}
