package net.cnoga.paint;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.cnoga.paint.controllers.MainController;

/**
 * The main launcher for the Paint(t) application.
 * <p>
 * Initializes the JavaFX runtime, loads the primary FXML layout,
 * sets up the main controller, and displays the primary stage.
 * Also sets application icons and initial window properties.
 * </p>
 *
 * <p>
 * Note: This class is the entry point for the application and should
 * not contain business logic beyond JavaFX initialization.
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
      PaintLauncher.class.getResource("fxml/main_gui.fxml"));
    Scene scene = new Scene(mainFxmlLoader.load(), 320, 240);

    // Initialize the ''brains'' of the whole operation
    MainController mainController = mainFxmlLoader.getController();
    mainController.init(primaryStage, scene);

    // Then the cosmetics
    try {
      primaryStage.getIcons().add(new Image(
        PaintLauncher.class.getResourceAsStream("icons/paint_icon.png")));
    } catch (NullPointerException nullPointerException) {
      System.err.println("Cannot find icon from path specified.");
    }

    primaryStage.setTitle("Pain(t)");
    primaryStage.setScene(scene);
    primaryStage.setMaximized(true);
    primaryStage.show();
  }
}
