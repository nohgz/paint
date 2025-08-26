package net.cnoga.paint.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class ShortcutBarController extends AbstractIOProvider {

  public Button shortcut_bar_new;
  public Button shortcut_bar_open;
  public Button shortcut_bar_save;

  public void onNewFile(ActionEvent actionEvent) {
    fileIOService.newFile();
  }

  public void onOpenFile(ActionEvent actionEvent) throws IOException {
    fileIOService.openFile();
  }

  public void onFileSave(ActionEvent actionEvent) {
    fileIOService.saveFile();
  }
}