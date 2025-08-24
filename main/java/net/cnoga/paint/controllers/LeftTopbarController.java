package net.cnoga.paint.controllers;

import javafx.event.ActionEvent;
import net.cnoga.paint.interfaces.FileIOAware;
import net.cnoga.paint.interfaces.ProgramIOAware;
import net.cnoga.paint.services.FileIOService;
import net.cnoga.paint.services.ProgramIOService;

public class LeftTopbarController implements FileIOAware, ProgramIOAware {

  private FileIOService fileIOService;
  private ProgramIOService programIOService;

  @Override
  public void setFileIOService(FileIOService fileIOService) {
    this.fileIOService = fileIOService;
  }
  @Override
  public void setProgramIOService(ProgramIOService programIOService) {
    this.programIOService = programIOService;
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

  public void onFileSaveAs(ActionEvent actionEvent) {
//    fileIOService.saveFileAs();
  }

  public void onAppExit(ActionEvent actionEvent) {
    programIOService.closeApp();
  }
}
