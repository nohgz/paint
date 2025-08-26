package net.cnoga.paint.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.events.ImageOpenEvent;
import net.cnoga.paint.events.FileSaveEvent;
import net.cnoga.paint.events.NewFileEvent;

public class FileIOService extends EventBusProvider {

  private Stage stage;

  public FileIOService(EventBus bus, Stage stage) {
    super(bus);
    this.stage = stage;
  }

  public void newFile() {
    bus.post(new NewFileEvent());
  }

  public void saveFile() {
    bus.post(new FileSaveEvent());
  }

  public void openFile(File file) throws IOException {
    Image image = new Image(new FileInputStream(file));
    bus.post(new ImageOpenEvent(image));
  }

  public void openFile() throws IOException {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Open");
    File selectedFile = chooser.showOpenDialog(stage);
    if (selectedFile != null) {
      System.out.println("[FileIOService] Opened file: " + selectedFile.getAbsolutePath() + ".");
      openFile(new File(selectedFile.getAbsolutePath()));
    } else {
      System.out.println("[FileIOService] Did not open a file / File invalid.");
    }
  }

  public void closeProgram() {
    this.stage.close();
  }
}
