package net.cnoga.paint;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.cnoga.paint.controllers.PaintGUIController;
import net.cnoga.paint.services.FileIOService;

public class PaintApplicationLauncher extends Application {

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
      PaintApplicationLauncher.class.getResource("paint_gui.fxml"));
    Scene scene = new Scene(mainFxmlLoader.load(), 320, 240);
    PaintGUIController mainController = mainFxmlLoader.getController();
    mainController.init(primaryStage);

    // Then the cosmetics
    try {
      primaryStage.getIcons().add(new Image(
        PaintApplicationLauncher.class.getResourceAsStream("icons/paint_icon.png")));
    } catch (NullPointerException nullPointerException) {
      System.err.println("Cannot find icon from path specified.");
    }

    primaryStage.setTitle("Pain(t)");
    primaryStage.setScene(scene);
    primaryStage.setMaximized(true);
    primaryStage.show();
  }
}
