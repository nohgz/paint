package net.cnoga.paint.controllers;

import javafx.event.ActionEvent;
import net.cnoga.paint.interfaces.FileIOAware;
import net.cnoga.paint.services.FileIOService;

public class ShortcutBarController implements FileIOAware {

  private FileIOService fileIOService;

  @Override
  public void setFileIOService(FileIOService fileIOService) {
    this.fileIOService = fileIOService;
  }

  public void onNewFile(ActionEvent actionEvent) {
    fileIOService.newFile();
  }

  public void onOpenFile(ActionEvent actionEvent) {
    fileIOService.openFile();
  }

  public void onFileSave(ActionEvent actionEvent) {
//    fileIOService.saveFile();
  }
}
