package net.cnoga.paint.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class ImageHelper {
  public static WritableImage loadWritableImage(String filePath) {
    try {
      FileInputStream inputStream = new FileInputStream(new File(filePath));
      Image image = new Image(inputStream);

      WritableImage writableImage = new WritableImage(
        (int) image.getWidth(),
        (int) image.getHeight()
      );

      PixelReader pixelReader = image.getPixelReader();
      writableImage.getPixelWriter().setPixels(
        0,0,
        (int) image.getWidth(),
        (int) image.getHeight(),
        pixelReader,
        0, 0
      );

      return writableImage;

    } catch (FileNotFoundException e) {
      System.err.println("Could not find file: " + filePath);
      return null;
    }
  }
}
