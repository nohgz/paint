package net.cnoga.paint.publisher;

import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.bus.EventBusProvider;
import net.cnoga.paint.events.FileOpenEvent;
import net.cnoga.paint.events.FileSaveAsEvent;
import net.cnoga.paint.events.FileSaveEvent;
import net.cnoga.paint.events.NewFileEvent;

/**
 * Publishes file I/O–related actions to the application's {@link EventBus}.
 * <p>
 * This class connects JavaFX controller actions (e.g., "New", "Open", "Save")
 * with the event-driven backend by posting corresponding events. It also provides
 * simple integration with the JavaFX {@link FileChooser} for file selection
 * and with the {@link Stage} for closing the application.
 * </p>
 *
 * @author cnoga
 * @version 1.0
 */
public class FileIOPublisher extends EventBusProvider {

  private Stage stage;

  public FileIOPublisher(EventBus bus, Stage stage) {
    super(bus);
    this.stage = stage;
  }

  public void newFile() {
    bus.post(new NewFileEvent());
  }

  public void saveFile() {
    bus.post(new FileSaveEvent());
  }

  public void saveFileAs() {
    bus.post(new FileSaveAsEvent());
  }

  public void openFile(File file) {
    bus.post(new FileOpenEvent(file));
  }

  public void openFile() throws IOException {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Open");
    File selectedFile = chooser.showOpenDialog(stage);
    if (selectedFile != null) {
      openFile(new File(selectedFile.getAbsolutePath()));
    } else {
      System.out.println("[FileIOService] Did not open a file / File invalid.");
    }
  }

  public void closeProgram() {
    // FIXME: This is a violation of my principle that publishers send events, and listeners handle them.
    // I have to make a program Publisher / Listener... but doing that adds a lot of annoying boilerplate.
    this.stage.close();
  }
}
