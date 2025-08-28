package net.cnoga.paint.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import net.cnoga.paint.services.FileIOPublisher;

public abstract class AbstractIOController {
  // classes that inherit this guy are capable of basic IO

  protected FileIOPublisher fileIOPublisher;

  public void initFileIOPublisher(FileIOPublisher fileIOPublisher) {
    this.fileIOPublisher = fileIOPublisher;
  }

  public void onNewFile(ActionEvent actionEvent) {
    fileIOPublisher.newImage();
  }

  public void onOpenFile(ActionEvent actionEvent) throws IOException {
    fileIOPublisher.openImage();
  }

  public void onFileSave(ActionEvent actionEvent) {
    fileIOPublisher.saveImage();
  }

  public void onFileSaveAs(ActionEvent actionEvent) {
    fileIOPublisher.saveImage();
  }
}