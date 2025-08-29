package net.cnoga.paint.util;

import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;

/**
 * Utility class providing helper methods for working with JavaFX painting components.
 * @author cnoga
 * @version 1.0
 */
public class PaintUtil {
  /**
   * Saves the current contents of a JavaFX {@link javafx.scene.canvas.Canvas} to a file as a PNG image.
   *
   * @param canvas the {@code Canvas} to save; must not be {@code null}
   * @param file   the target {@code File} where the image will be written;
   *
   * @implNote The snapshot uses the current size of the canvas
   *           (width × height as integers). The output format is fixed to "png".
   */
  public static void saveCanvasToFile(Canvas canvas, File file) {
    WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
    canvas.snapshot(null, writableImage);
    try {
      ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

