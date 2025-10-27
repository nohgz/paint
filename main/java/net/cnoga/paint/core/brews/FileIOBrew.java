package net.cnoga.paint.core.brews;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import net.cnoga.paint.core.bus.EventBus;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.request.FileOpenRequest;
import net.cnoga.paint.core.bus.events.response.FileOpenedEvent;
import net.cnoga.paint.core.bus.events.response.WorkspaceSavedAsEvent;
import net.cnoga.paint.core.bus.events.response.WorkspaceSavedEvent;
import net.cnoga.paint.core.workspace.Workspace;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;

/**
 * Publishes file I/O–related actions to the application's {@link EventBus}.
 * <p>
 * This class connects JavaFX controller actions (e.g., "New", "Open", "Save") with the event-driven
 * backend by posting corresponding events.
 * </p>
 *
 * @author cnoga
 * @version 1.0
 */

@EventBusSubscriber
public class FileIOBrew extends EventBusPublisher {

  private final Stage stage;

  /**
   * Constructs and registers this service with the event bus.
   */
  public FileIOBrew(Stage stage) {
    this.stage = stage;
    bus.register(this);
  }

  /**
   * Opens a file via FileChooser and posts a FileOpenedEvent.
   */
  @SubscribeEvent
  private void onOpenFile(FileOpenRequest req) {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Open");
    File selectedFile = chooser.showOpenDialog(stage);
    if (selectedFile != null) {
      bus.post(new FileOpenedEvent(new File(selectedFile.getAbsolutePath())));
    } else {
      System.out.println("[FileIOService] Did not open a file / File invalid.");
    }
  }

  /**
   * Saves workspace with a new filename, showing FileChooser.
   */
  @SubscribeEvent
  private void onWorkspaceSavedAs(WorkspaceSavedAsEvent evt) {
    Workspace ws = evt.workspace();
    if (ws == null) {
      return;
    }

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Workspace As...");

    String originalExt = "";
    if (ws.getFile() != null) {
      originalExt = getFileExtension(ws.getFile().getName()).toLowerCase();
    }

    boolean originallyLossy = originalExt.equals("jpg") || originalExt.equals("jpeg");

    fileChooser.getExtensionFilters().addAll(
      new ExtensionFilter("PNG Image", "*.png"),
      new ExtensionFilter("BMP Image", "*.bmp")
    );

    if (originallyLossy) {
      fileChooser.getExtensionFilters().add(
        new ExtensionFilter("JPEG Image", "*.jpg", "*.jpeg")
      );
    } else {
      fileChooser.getExtensionFilters().add(
        new ExtensionFilter("JPEG Image (Warning: May be lossy)", "*.jpg", "*.jpeg")
      );
    }

    File file = fileChooser.showSaveDialog(stage);
    if (file != null) {
      ws.setFile(file);
      try {
        saveWorkspace(ws);
        ws.setDirty(false);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Saves workspace to its current file.
   */
  @SubscribeEvent
  private void onWorkspaceSaved(WorkspaceSavedEvent evt) {
    Workspace ws = evt.workspace();

    if (ws.getFile() != null) {
      try {
        saveWorkspace(ws);
        ws.setDirty(false);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Saves the workspace to disk based on its file extension.
   */
  public void saveWorkspace(Workspace ws) throws IOException, ImageWriteException {
    File file = ws.getFile();
    if (ws == null || file == null) {
      return;
    }

    Canvas base = ws.getBaseLayer();

    WritableImage writableImage = new WritableImage(
      (int) base.getWidth(),
      (int) base.getHeight()
    );

    SnapshotParameters params = new SnapshotParameters();
    params.setFill(Color.TRANSPARENT);
    base.snapshot(params, writableImage);

    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
    String ext = getFileExtension(file.getName()).toLowerCase();

    switch (ext) {
      case "jpg":
      case "jpeg":
        saveAsJPEG(bufferedImage, file);
        break;
      case "bmp":
        Imaging.writeImage(bufferedImage, file, ImageFormats.BMP);
        break;
      case "png":
      default:
        Imaging.writeImage(bufferedImage, file, ImageFormats.PNG);
        break;
    }
  }

  /**
   * Saves a BufferedImage as JPEG.
   */
  private void saveAsJPEG(BufferedImage bufferedImage, File file) throws IOException {
    BufferedImage rgbImage = new BufferedImage(
      bufferedImage.getWidth(),
      bufferedImage.getHeight(),
      BufferedImage.TYPE_INT_RGB
    );
    Graphics2D g = rgbImage.createGraphics();
    g.drawImage(bufferedImage, 0, 0, null);
    g.dispose();
    ImageIO.write(rgbImage, "jpg", file);
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
}
