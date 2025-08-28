package net.cnoga.paint.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.bus.EventBusProvider;
import net.cnoga.paint.events.ImageOpenEvent;
import net.cnoga.paint.events.ImageSaveEvent;
import net.cnoga.paint.events.NewImageEvent;

public class FileIOPublisher extends EventBusProvider {

  private Stage stage;

  public FileIOPublisher(EventBus bus, Stage stage) {
    super(bus);
    this.stage = stage;
  }

  public void newImage() {
    bus.post(new NewImageEvent());
  }

  public void saveImage() {
    bus.post(new ImageSaveEvent());
  }

  public void saveImageAs() {
    bus.post(new ImageSaveEvent());
  }

  public void openImage(File file) throws IOException {
    Image image = new Image(new FileInputStream(file));
    bus.post(new ImageOpenEvent(image));
  }

  public void openImage() throws IOException {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Open");
    File selectedFile = chooser.showOpenDialog(stage);
    if (selectedFile != null) {
      System.out.println("[FileIOService] Opened file: " + selectedFile.getAbsolutePath() + ".");
      openImage(new File(selectedFile.getAbsolutePath()));
    } else {
      System.out.println("[FileIOService] Did not open a file / File invalid.");
    }
  }

  public void closeProgram() {
    this.stage.close();
  }
}
