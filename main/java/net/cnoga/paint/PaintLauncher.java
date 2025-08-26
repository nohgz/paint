package net.cnoga.paint;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.cnoga.paint.controllers.MainGUIController;

public class PaintLauncher extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  /**
   * The Launcher for my Pain(t).
   */
  @Override
  public void start(Stage primaryStage) throws IOException {
    // Start with the fundamental stuff
    FXMLLoader mainFxmlLoader = new FXMLLoader(
      PaintLauncher.class.getResource("fxml/main_gui.fxml"));
    Scene scene = new Scene(mainFxmlLoader.load(), 320, 240);

    // Initialize the ''brains'' of the whole operation
    MainGUIController mainController = mainFxmlLoader.getController();
    mainController.init(primaryStage);

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
