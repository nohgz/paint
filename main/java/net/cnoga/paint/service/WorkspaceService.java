package net.cnoga.paint.service;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.FileSaveAsRequest;
import net.cnoga.paint.events.request.FileSaveRequest;
import net.cnoga.paint.events.request.InitWorkspaceRequest;
import net.cnoga.paint.events.request.NewFileRequest;
import net.cnoga.paint.events.request.SaveStateRequest;
import net.cnoga.paint.events.response.FileOpenedEvent;
import net.cnoga.paint.events.response.GetSaveStateEvent;
import net.cnoga.paint.events.response.ToolChangedEvent;
import net.cnoga.paint.tool.Tool;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;

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
 *   <li>Responds to {@link FileOpenedEvent} by loading the selected image into a canvas.</li>
 *   <li>Responds to {@link NewFileRequest} by creating a new blank canvas with default content.</li>
 *   <li>Handles {@link FileSaveRequest} and {@link FileSaveAsRequest} by saving the current canvas
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
public class WorkspaceService extends EventBusPublisher {

  private ScrollPane scrollPane;
  private StackPane stackPane;
  private Group canvasGroup;
  private Canvas canvas;
  private File currentFile;
  private Double zoomFactor;
  private Tool currentTool;
  private Boolean dirtyFlag = false;

  public WorkspaceService() {
    bus.register(this);
  }

  @SubscribeEvent
  private void onToolSelected(ToolChangedEvent event) {
    this.currentTool = event.tool();

    // enable panning and scrolling capability
    scrollPane.setPannable(Objects.equals(currentTool.getName(), "Pan") && canvasGroup != null);
  }

  @SubscribeEvent
  private void onInitWorkspace(InitWorkspaceRequest initWorkspaceRequest) {
    this.canvasGroup = initWorkspaceRequest.workspaceCanvasGroup();
    this.scrollPane = initWorkspaceRequest.workspaceScrollPane();
    this.stackPane = initWorkspaceRequest.workspaceStackPane();
  }

  /**
   * Handles opening an image file into the canvas.
   *
   * @param fileOpenedEvent event containing the file to open
   * @throws FileNotFoundException if the file cannot be read
   */
  @SubscribeEvent
  private void onFileOpened(FileOpenedEvent fileOpenedEvent) throws FileNotFoundException {
    currentFile = fileOpenedEvent.file();
    Image img = new Image(new FileInputStream(currentFile));
    loadCanvas(img.getWidth(), img.getHeight(), gc -> gc.drawImage(img, 0, 0));
  }

  /**
   * Creates a new blank canvas with default placeholder content.
   *
   * @param newFileRequest event signaling the creation of a new file
   */
  @SubscribeEvent
  private void newFile(NewFileRequest newFileRequest) {
    currentFile = null;
    loadCanvas(1000, 700, gc -> {
      gc.setFill(Color.WHITE);
      gc.fillRect(0, 0, 1000, 700);
    });
  }



  /**
   * Prompts the user to select a file path and saves the canvas there.
   *
   * @param fileSaveAsRequest event signaling a save-as operation
   */
  @SubscribeEvent
  private void onImageSaveAs(FileSaveAsRequest fileSaveAsRequest) {
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

    initCanvasCapability(canvas, gc);

    // Clear and reuse the FXML-defined group + stackpane
    canvasGroup.getChildren().clear();
    canvasGroup.getChildren().add(canvas);
    scrollPane.setContent(stackPane);

    // Reset zoom
    zoomFactor = 1.0;

    // Zoom handler
    scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
      if (!Objects.equals(currentTool.getName(), "Pan")) return;

      if (event.isControlDown()) {
        double oldZoom = zoomFactor;

        if (event.getDeltaY() > 0) {
          zoomFactor *= 2;
        } else {
          zoomFactor /= 2;
        }

        zoomFactor = Math.max(0.125, Math.min(zoomFactor, 4));
        System.out.println("zF:" + zoomFactor);

        // Apply scale to the group
        canvasGroup.setScaleX(zoomFactor);
        canvasGroup.setScaleY(zoomFactor);

        // Optional: keep zoom centered
        double mouseX = event.getX();
        double mouseY = event.getY();
        double adjustmentX = (mouseX + scrollPane.getHvalue() * scrollPane.getContent().getBoundsInLocal().getWidth()) * (zoomFactor / oldZoom - 1);
        double adjustmentY = (mouseY + scrollPane.getVvalue() * scrollPane.getContent().getBoundsInLocal().getHeight()) * (zoomFactor / oldZoom - 1);

        scrollPane.setHvalue(scrollPane.getHvalue() + adjustmentX / scrollPane.getContent().getBoundsInLocal().getWidth());
        scrollPane.setVvalue(scrollPane.getVvalue() + adjustmentY / scrollPane.getContent().getBoundsInLocal().getHeight());

        event.consume();
      }
    });
    dirtyFlag = false;
  }

  /**
   * Saves the current canvas to the last known file location,
   * or prompts the user to choose a file if none exists.
   *
   * @param fileSaveRequest event signaling a save operation
   */
  @SubscribeEvent
  private void onImageSave(FileSaveRequest fileSaveRequest) {
    if (canvas == null) return;

    if (currentFile != null) {
      saveCanvasToFile(canvas, currentFile);
    } else {
      saveCanvasAs();
    }
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
      new FileChooser.ExtensionFilter("PNG Image", "*.png"));
    fileChooser.getExtensionFilters().add(
      new FileChooser.ExtensionFilter("BMP Image", "*.bmp"));
    fileChooser.getExtensionFilters().add(
      new FileChooser.ExtensionFilter("JPG Image", "*.jpg"));

    File file = fileChooser.showSaveDialog(scrollPane.getScene().getWindow());
    if (file != null) {
      saveCanvasToFile(canvas, file);
      currentFile = file;
    }
  }

  /**
   * Saves the current contents of a JavaFX {@link javafx.scene.canvas.Canvas} to a file.
   *
   * @param canvas the {@code Canvas} to save; must not be {@code null}
   * @param file   the target {@code File} where the image will be written
   * @implNote The snapshot uses the current size of the canvas (width × height as integers).
   *           The output format is inferred from the file extension (png, bmp, jpg/jpeg).
   */
  public void saveCanvasToFile(Canvas canvas, File file) {
    if (canvas == null || file == null) return;

    WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
    canvas.snapshot(null, writableImage);

    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
    String ext = getFileExtension(file.getName()).toLowerCase();

    try {
      switch (ext) {
        case "jpg":
        case "jpeg":
          // Flatten alpha for JPEG
          BufferedImage rgbImage = new BufferedImage(bufferedImage.getWidth(),
            bufferedImage.getHeight(),
            BufferedImage.TYPE_INT_RGB);
          Graphics2D g = rgbImage.createGraphics();
          g.drawImage(bufferedImage, 0, 0, null);
          g.dispose();
          ImageIO.write(rgbImage, "jpg", file);
          break;
        case "bmp":
          Imaging.writeImage(bufferedImage, file, ImageFormats.BMP);
          break;
        case "png":
        default:
          Imaging.writeImage(bufferedImage, file, ImageFormats.PNG);
          break;
      }
      dirtyFlag = false;
    } catch (IOException | ImageWriteException e) {
      e.printStackTrace();
    }
  }

  private String getFileExtension(String filename) {
    int lastDot = filename.lastIndexOf('.');
    return (lastDot == -1) ? "" : filename.substring(lastDot + 1);
  }

  private void initCanvasCapability(Canvas canvas, GraphicsContext gc) {
    canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
      e -> {
        currentTool.onMousePressed(gc, e.getX(), e.getY());
        if (!"Pan".equals(currentTool.getName())) {
          dirtyFlag = true; // mark dirty only if the tool modifies the canvas
        }
      });

    canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
      e -> {
        currentTool.onMouseDragged(gc, e.getX(), e.getY());
        if (!"Pan".equals(currentTool.getName())) {
          dirtyFlag = true; // mark dirty only if the tool modifies the canvas
        }
      });

    canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
      e -> currentTool.onMouseReleased(gc, e.getX(), e.getY()));
  }

  @SubscribeEvent
  private void onSaveStateRequest(SaveStateRequest req) {
    bus.post(new GetSaveStateEvent(dirtyFlag));
  }
}
