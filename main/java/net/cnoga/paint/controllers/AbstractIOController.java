package net.cnoga.paint.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import net.cnoga.paint.publisher.FileIOPublisher;

/**
 * Abstract base class for JavaFX controllers that need basic file
 * input/output functionality.
 * <p>
 * Provides event-handler methods for common file operations such as
 * creating, opening, saving, and saving-as. Subclasses can connect
 * UI elements (e.g. menu items, buttons) directly to these methods
 * in FXML, delegating the actual work to a {@link FileIOPublisher}.
 */
public abstract class AbstractIOController {
  // classes that inherit this guy are capable of basic IO

  protected FileIOPublisher fileIOPublisher;

  /**
   * Initializes this controller with a {@link FileIOPublisher}.
   *
   * @param fileIOPublisher the publisher that dispatches file-related events
   */
  public void initFileIOPublisher(FileIOPublisher fileIOPublisher) {
    this.fileIOPublisher = fileIOPublisher;
  }

  /**
   * Handles a "new file" action from the UI.
   * Posts a {@code NewFileEvent} through the {@link FileIOPublisher}.
   *
   * @param actionEvent the JavaFX action event that triggered this call
   */
  public void onNewFile(ActionEvent actionEvent) {
    fileIOPublisher.newFile();
  }

  /**
   * Handles an "open file" action from the UI.
   * Displays a file chooser dialog and posts a {@code FileOpenEvent}
   * if the user selects a file.
   *
   * @param actionEvent the JavaFX action event that triggered this call
   * @throws IOException if an I/O error occurs while opening the file
   */
  public void onOpenFile(ActionEvent actionEvent) throws IOException {
    fileIOPublisher.openFile();
  }

  /**
   * Handles a "save file" action from the UI.
   * Posts a {@code FileSaveEvent} through the {@link FileIOPublisher}.
   *
   * @param actionEvent the JavaFX action event that triggered this call
   */
  public void onFileSave(ActionEvent actionEvent) {
    fileIOPublisher.saveFile();
  }

  /**
   * Handles a "save file as" action from the UI.
   * Displays a file chooser dialog and posts a {@code FileSaveAsEvent}
   * if the user selects a location.
   *
   * @param actionEvent the JavaFX action event that triggered this call
   */
  public void onFileSaveAs(ActionEvent actionEvent) {
    fileIOPublisher.saveFileAs();
  }
}