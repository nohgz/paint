package net.cnoga.paint.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

/**
 * Doesn't do anything at the moment. But will prove useful as a organizational tool later.
 */
public class ImageHelper {


  public static Image loadImage(String filePath) {
    try {
      FileInputStream inputStream = new FileInputStream(filePath);
      return new Image(inputStream);
    } catch (FileNotFoundException e) {
      System.err.println("Could not find file: " + filePath);
      return null;
    }
  }

  public static Canvas imageToCanvas(Image image) {
    return new Canvas();
  }

  public static Canvas fileToCanvas(String filepath) {
    return new Canvas();
  }
}
