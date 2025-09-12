package net.cnoga.paint;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.controllers.MainController;
import net.cnoga.paint.events.request.NewFileRequest;
import net.cnoga.paint.events.request.OpenHistoryRequest;
import net.cnoga.paint.events.request.OpenLayersRequest;
import net.cnoga.paint.events.request.OpenToolsRequest;
import net.cnoga.paint.events.response.ToolChangedEvent;
import net.cnoga.paint.tool.PaintTools;

/**
 * The main launcher for the Paint(t) application.
 * <p>
 * Initializes the JavaFX runtime, loads the primary FXML layout, sets up the main controller, and
 * displays the primary stage. Also sets application icons and initial window properties.
 * </p>
 *
 * <p>
 * Note: This class is the entry point for the application and should not contain business logic
 * beyond JavaFX initialization.
 * </p>
 *
 * @author cnoga
 * @version 1.0
 */
public class PaintLauncher extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    // Start with the fundamental stuff
    FXMLLoader mainFxmlLoader = new FXMLLoader(
      PaintLauncher.class.getResource("fxml/window/main_gui.fxml"));
    Scene primaryScene = new Scene(mainFxmlLoader.load(), 320, 240);

    // Initialize the ''brains'' of the whole operation
    MainController mainController = mainFxmlLoader.getController();

    EventBus bus = EventBus.getInstance();
    mainController.init(primaryStage, primaryScene);

    // Then the cosmetics
    try {
      primaryStage.getIcons().add(new Image(
        PaintLauncher.class.getResourceAsStream("icons/paint_icon.png")));
    } catch (NullPointerException nullPointerException) {
      System.err.println("Cannot find icon from path specified.");
    }

    primaryStage.setTitle("Pain(t)");
    primaryStage.setScene(primaryScene);
    primaryStage.setMaximized(true);
    primaryStage.show();

    // Initial startup state
    bus.post(new ToolChangedEvent(PaintTools.BRUSH));
    bus.post(new NewFileRequest());
    bus.post(new OpenHistoryRequest());
    bus.post(new OpenLayersRequest());
    bus.post(new OpenToolsRequest());
  }
}
