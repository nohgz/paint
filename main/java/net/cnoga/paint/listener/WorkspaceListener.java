package net.cnoga.paint.listener;

import static net.cnoga.paint.util.PaintUtil.saveCanvasToFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

/**
 * Listens for file and workspace-related events on the application event bus and
 * updates the drawing canvas accordingly.
 *
 * <p>This class connects the event-driven backend logic to the JavaFX UI workspace.
 * It manages the lifecycle of the {@link Canvas}, including loading images into it,
 * creating new blank files, and handling save/save-as operations.</p>
 *
 * <p>Main responsibilities:</p>
 * <ul>
 *   <li>Responds to {@link FileOpenEvent} by loading the selected image into a canvas.</li>
 *   <li>Responds to {@link NewFileEvent} by creating a new blank canvas with default content.</li>
 *   <li>Handles {@link FileSaveEvent} and {@link FileSaveAsEvent} by saving the current canvas
 *       to disk via a {@link FileChooser} or the existing file path.</li>
 *   <li>Ensuring the UI components ({@link ScrollPane}, {@link StackPane}, {@link Group})
 *       correctly reflect the current canvas state.</li>
 * </ul>
 * </p>
 *
 * @author cnoga
 * @version 1.0
 */
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

  /**
   * Handles opening an image file into the canvas.
   *
   * @param fileOpenEvent event containing the file to open
   * @throws FileNotFoundException if the file cannot be read
   */
  @SubscribeEvent
  private void onFileOpened(FileOpenEvent fileOpenEvent) throws FileNotFoundException {
    currentFile = fileOpenEvent.file();
    Image img = new Image(new FileInputStream(currentFile));
    loadCanvas(img.getWidth(), img.getHeight(), gc -> gc.drawImage(img, 0, 0));
  }

  /**
   * Creates a new blank canvas with default placeholder content.
   *
   * @param newFileEvent event signaling the creation of a new file
   */
  @SubscribeEvent
  private void newFile(NewFileEvent newFileEvent) {
    loadCanvas(1000, 700, gc -> {
      gc.setFill(Color.WHITE);
      gc.fillRect(0, 0, 1000, 700);
    });
  }

  /**
   * Saves the current canvas to the last known file location,
   * or prompts the user to choose a file if none exists.
   *
   * @param fileSaveEvent event signaling a save operation
   */
  @SubscribeEvent
  private void onImageSave(FileSaveEvent fileSaveEvent) {
    if (canvas == null) return;

    if (currentFile != null) {
      saveCanvasToFile(canvas, currentFile);
    } else {
      System.out.println("ah shit !!!");
      saveCanvasAs();
    }
  }

  /**
   * Prompts the user to select a file path and saves the canvas there.
   *
   * @param fileSaveAsEvent event signaling a save-as operation
   */
  @SubscribeEvent
  private void onImageSaveAs(FileSaveAsEvent fileSaveAsEvent) {
    saveCanvasAs();
  }

  /**
   * Initializes and draws a new canvas of the specified size,
   * then attaches it to the UI workspace.
   *
   * @param width width of the canvas in pixels
   * @param height height of the canvas in pixels
   * @param artist lambda to perform drawing operations on the new canvas
   */
  private void loadCanvas(double width, double height, Consumer<GraphicsContext> artist) {
    // Create canvas
    canvas = new Canvas(width, height);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.setImageSmoothing(false);
    artist.accept(gc);

    // Add canvas to Group
    canvasGroup.getChildren().clear();
    canvasGroup.getChildren().add(canvas);

    // Set StackPane size to match image
    stackPane.setPrefWidth(width);
    stackPane.setPrefHeight(height);

    scrollPane.layout();
  }

  /**
   * Prompts the user to select a file path, saves the current canvas as PNG,
   * and updates {@code currentFile}.
   */
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
