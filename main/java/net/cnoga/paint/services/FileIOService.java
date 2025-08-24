package net.cnoga.paint.services;

import java.io.File;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FileIOService handles the actual functionality of the calls from controllers.
 */
public class FileIOService  {

  private final Stage primaryStage;
  // create a "file-to-save" parameter here
  private WritableImage image;

  public FileIOService(Stage stage) {
    System.out.println("I AM BEING MADE!!!!!!");
    this.primaryStage = stage;
  }

  /**
   * This is a unified way to open files utilizing the parent scene.
   * @return selectedFile
   */
  public File openFile() {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Open");
    File selectedFile = chooser.showOpenDialog(primaryStage);
    if (selectedFile != null) {
      System.out.println("Opened file: " + selectedFile.getAbsolutePath());
    }


    return selectedFile;
  }

  public void saveFile(File file) {
    System.out.println("TODO: Save file!");
    // TODO: Full Save Logic
  }

  /**
   * This effectively acts like a glorified save dialog opener.
   * @param file
   */
  public void saveFileAs(File file) {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Save File As");
    File selectedFile = chooser.showSaveDialog(primaryStage);

    if (selectedFile != null) {
      System.out.println("Saving file as: " + selectedFile.getAbsolutePath());
    }
  }

  public void newFile() {
    System.out.println("TODO: New file opened!");
  }

}