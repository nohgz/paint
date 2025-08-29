package net.cnoga.paint.util;

import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;

public class PaintUtil {
  public static void saveCanvasToFile(Canvas canvas, File file) {
    WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
    canvas.snapshot(null, writableImage);
    try {
      ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
      System.out.println("Saved image to: " + file.getAbsolutePath());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

