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
 * Service responsible for managing the drawing workspace and connecting the
 * event-driven backend with the JavaFX UI.
 *
 * <p>This class acts as the central controller for the canvas lifecycle, including
 * creation, modification, and persistence of images. It listens for events on the
 * {@link net.cnoga.paint.bus.EventBus} and updates the workspace UI components
 * accordingly.</p>
 *
 * <h2>Main Responsibilities:</h2>
 * <ul>
 *   <li>Initializing workspace UI components such as {@link ScrollPane},
 *       {@link StackPane}, and {@link Group}.</li>
 *   <li>Creating new blank canvases or loading existing image files.</li>
 *   <li>Handling save and save-as operations, including format-specific logic
 *       for PNG, BMP, and JPEG.</li>
 *   <li>Managing the current drawing tool and its interaction with the canvas.</li>
 *   <li>Tracking whether the canvas has unsaved modifications (the "dirty" flag).</li>
 *   <li>Providing zooming and panning capabilities when the Pan tool is active.</li>
 * </ul>
 *
 * <p>Supported file formats:</p>
 * <ul>
 *   <li><b>PNG</b> — lossless with transparency.</li>
 *   <li><b>BMP</b> — uncompressed bitmap format.</li>
 *   <li><b>JPEG</b> — lossy, transparency automatically flattened to white.</li>
 * </ul>
 *
 * @author cnoga
 * @version 1.1
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

  /**
   * Handles tool selection changes and enables/disables panning based on whether the active tool is
   * {@code Pan}.
   *
   * @param event the {@link ToolChangedEvent} containing the newly selected tool
   */
  @SubscribeEvent
  private void onToolSelected(ToolChangedEvent event) {
    this.currentTool = event.tool();

    // enable panning and scrolling capability
    scrollPane.setPannable(Objects.equals(currentTool.getName(), "Pan") && canvasGroup != null);
  }

  /**
   * Initializes the workspace UI by linking this service to the FXML-defined {@link ScrollPane},
   * {@link StackPane}, and {@link Group}.
   *
   * @param initWorkspaceRequest event providing the workspace nodes
   */
  @SubscribeEvent
  private void onInitWorkspace(InitWorkspaceRequest initWorkspaceRequest) {
    this.canvasGroup = initWorkspaceRequest.workspaceCanvasGroup();
    this.scrollPane = initWorkspaceRequest.workspaceScrollPane();
    this.stackPane = initWorkspaceRequest.workspaceStackPane();
  }


  /**
   * Loads an image file into the canvas and replaces the current canvas.
   *
   * @param fileOpenedEvent event containing the file to open
   * @throws FileNotFoundException if the file does not exist or cannot be read
   */
  @SubscribeEvent
  private void onFileOpened(FileOpenedEvent fileOpenedEvent) throws FileNotFoundException {
    currentFile = fileOpenedEvent.file();
    Image img = new Image(new FileInputStream(currentFile));
    loadCanvas(img.getWidth(), img.getHeight(), gc -> gc.drawImage(img, 0, 0));
  }

  /**
   * Creates a new blank canvas with default dimensions (1000×700)
   * and fills it with a white background.
   *
   * @param newFileRequest event signaling a new file creation
   */
  @SubscribeEvent
  private void newFile(NewFileRequest newFileRequest) {
    currentFile = null;
    // The size is hardcoded right now. I might make an event that creates a popup
    // that asks for the user's input.
    loadCanvas(1000, 700, gc -> {
      gc.setFill(Color.WHITE);
      gc.fillRect(0, 0, 1000, 700);
    });
  }

  /**
   * Handles a "Save As" request by prompting the user for a file
   * location and format via a {@link FileChooser}.
   *
   * @param fileSaveAsRequest event signaling a save-as operation
   */
  @SubscribeEvent
  private void onImageSaveAs(FileSaveAsRequest fileSaveAsRequest) {
    saveCanvasAs();
  }


  /**
   * Replaces the current canvas with a new one of the given dimensions
   * and draws initial content provided by the supplied artist function.
   *
   * @param width  width of the new canvas in pixels
   * @param height height of the new canvas in pixels
   * @param artist callback invoked with the {@link GraphicsContext} to perform initial drawing
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
   * Saves the current canvas to the existing file path, or prompts
   * the user to choose a file if no path is set.
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
   * Prompts the user to select a file path and saves the current canvas.
   * <p>The chosen file extension determines the output format (PNG, BMP, JPEG).</p>
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
   * Saves the contents of the given canvas to the specified file.
   * <p>
   * File format is determined from the file extension:
   * <ul>
   *   <li><b>PNG</b>: Preserves alpha transparency.</li>
   *   <li><b>BMP</b>: Saved using Apache Commons Imaging.</li>
   *   <li><b>JPEG</b>: Alpha is flattened to white before saving.</li>
   * </ul>
   * </p>
   *
   * @param canvas the {@link Canvas} to save; must not be {@code null}
   * @param file   the output file; extension determines format
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

  /**
   * Extracts the lowercase file extension (without dot) from a filename.
   *
   * @param filename the filename to inspect
   * @return the file extension, or an empty string if none found
   */
  private String getFileExtension(String filename) {
    int lastDot = filename.lastIndexOf('.');
    return (lastDot == -1) ? "" : filename.substring(lastDot + 1);
  }

  /**
   * Wires up mouse event handlers for the given canvas so that the active {@link Tool} can handle
   * user drawing input.
   *
   * <p>If the current tool is not {@code Pan}, the canvas is marked
   * as dirty when pressed or dragged.</p>
   *
   * @param canvas the {@link Canvas} to initialize
   * @param gc     the {@link GraphicsContext} for drawing
   */
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

  /**
   * Handles requests for the current save state by publishing a {@link GetSaveStateEvent}
   * containing the dirty flag.
   *
   * @param req the {@link SaveStateRequest} event
   */
  @SubscribeEvent
  private void onSaveStateRequest(SaveStateRequest req) {
    bus.post(new GetSaveStateEvent(dirtyFlag));
  }
}
