package net.cnoga.paint.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import net.cnoga.paint.publisher.FileIOPublisher;

public abstract class AbstractIOController {
  // classes that inherit this guy are capable of basic IO

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