package net.cnoga.paint.service;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.FileOpenRequest;
import net.cnoga.paint.events.response.FileOpenedEvent;

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

@EventBusSubscriber
public class FileIOService extends EventBusPublisher {
  private final Stage stage;

  public FileIOService(Stage stage) {
    this.stage = stage;
    bus.register(this);
  }

  @SubscribeEvent
  public void onOpenFileRequest(FileOpenRequest fileOpenRequest) {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Open");
    File selectedFile = chooser.showOpenDialog(stage);
    if (selectedFile != null) {
      bus.post(new FileOpenedEvent(new File(selectedFile.getAbsolutePath())));
    } else {
      System.out.println("[FileIOService] Did not open a file / File invalid.");
    }
  }
}
