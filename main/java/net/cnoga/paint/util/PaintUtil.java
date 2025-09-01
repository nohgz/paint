package net.cnoga.paint.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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

  public static Stage createSubwindow(String title, String fxmlPath, Stage mainStage) {
    URL fxmlUrl = PaintUtil.class.getResource(fxmlPath);
    if (fxmlUrl == null) {
      throw new IllegalArgumentException(
        "FXML not found at path: " + fxmlPath
      );
    }

    try {
      FXMLLoader loader = new FXMLLoader(fxmlUrl);
      Scene scene = new Scene(loader.load());

      Stage stage = new Stage();
      stage.setTitle(title);
      stage.setScene(scene);
      stage.initStyle(StageStyle.UTILITY);

      stage.initOwner(mainStage);
      stage.initModality(Modality.NONE);
      stage.setAlwaysOnTop(true);

      return stage;
    } catch (IOException e) {
      System.err.println("Failed to load FXML: " + fxmlPath);
      e.printStackTrace();
      return null;
    }
  }
}

