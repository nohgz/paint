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
 *
 * @author cnoga
 * @version 1.0
 */
public abstract class AbstractIOController {

  protected FileIOPublisher fileIOPublisher;

  public void initFileIOPublisher(FileIOPublisher fileIOPublisher) {
    this.fileIOPublisher = fileIOPublisher;
  }

  public void onNewFile(ActionEvent actionEvent) {
    fileIOPublisher.newFile();
  }

  public void onOpenFile(ActionEvent actionEvent) throws IOException {
    fileIOPublisher.openFile();
  }

  public void onFileSave(ActionEvent actionEvent) {
    fileIOPublisher.saveFile();
  }

  public void onFileSaveAs(ActionEvent actionEvent) {
    fileIOPublisher.saveFileAs();
  }
}