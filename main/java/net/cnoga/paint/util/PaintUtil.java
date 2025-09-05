package net.cnoga.paint.util;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Utility class providing helper methods for Paint(t).
 * @author cnoga
 * @version 1.0
 */
public class PaintUtil {

  public static Stage createSubwindow(String title, String fxmlPath, Stage mainStage, Boolean resizable, Double x, Double y) {
    URL fxmlUrl = PaintUtil.class.getResource(fxmlPath);
    if (fxmlUrl == null) {
      throw new IllegalArgumentException(
        "FXML not found at path: " + fxmlPath
      );
    }

    try {
      FXMLLoader loader = new FXMLLoader(fxmlUrl);
      Scene scene = new Scene(loader.load());

      Stage sub = new Stage();
      sub.setTitle(title);
      sub.setScene(scene);
      sub.initStyle(StageStyle.UTILITY);
      sub.setResizable(resizable);
      sub.initOwner(mainStage);
      sub.initModality(Modality.NONE);

      sub.setX(x);
      sub.setY(y);

      // Keep palette above the main window only while app is focused
      mainStage.focusedProperty().addListener((obs, was, is) -> {
        sub.setAlwaysOnTop(is);   // on when app focused, off when not
      });

      return sub;
    } catch (IOException e) {
      System.err.println("Failed to load FXML: " + fxmlPath);
      e.printStackTrace();
      return null;
    }
  }

  public static Stage createToggledSubwindow(String title, String fxmlPath, Stage mainStage, Boolean resizable, Double x, Double y, ToggleButton toggleButton) {
    Stage subwindow = createSubwindow(title, fxmlPath, mainStage, resizable, x, y);

    subwindow.setOnCloseRequest(windowEvent -> {
      try {
        toggleButton.fire();
      } catch (NullPointerException e) {
        System.err.println("The button provided is null and can't be toggled!");
      }
    });

    return subwindow;
  }
  public static void setSubwindowSpawnPoint(Stage subStage, Stage mainStage, AnchorTypes anchorType) {
    subStage.setOnShown(e -> {
      // The magic numbers here are to account for the size of the other bars
      // so that the subwindows are opened over the main canvas.

      // I don't like this approach its stupid and dumb.
      double x = mainStage.getX();
      double y = mainStage.getY() + 110;

      double mainWidth = mainStage.getWidth();
      double mainHeight = mainStage.getHeight()-120;
      double subWidth = subStage.getWidth();
      double subHeight = subStage.getHeight();

      // Default positions
      double targetX = x;
      double targetY = y;

      switch (anchorType) {
        case TOP_LEFT:
          // Default already fine
          break;

        case TOP_CENTER:
          targetX += (mainWidth - subWidth) / 2;
          break;

        case TOP_RIGHT:
          targetX += mainWidth - subWidth;
          break;

        case MIDDLE_LEFT:
          targetY += (mainHeight - subHeight) / 2;
          break;

        case MIDDLE_CENTER:
          targetX += (mainWidth - subWidth) / 2;
          targetY += (mainHeight - subHeight) / 2;
          break;

        case MIDDLE_RIGHT:
          targetX += mainWidth - subWidth;
          targetY += (mainHeight - subHeight) / 2;
          break;

        case BOTTOM_LEFT:
          targetY += mainHeight - subHeight;
          break;

        case BOTTOM_CENTER:
          targetX += (mainWidth - subWidth) / 2;
          targetY += mainHeight - subHeight;
          break;

        case BOTTOM_RIGHT:
          targetX += mainWidth - subWidth;
          targetY += mainHeight - subHeight;
          break;
      }

      //FIXME: This logic is fucking dogshit. I need to fix it.
      if (subWidth > mainWidth) {
        if (anchorType.toString().contains("LEFT")) {
          targetX = x - subWidth; // push left of main
        } else if (anchorType.toString().contains("RIGHT")) {
          System.out.println("PUSH RIGHT OF MAIN");
          targetX = x + mainWidth + subWidth; // push right of main
        } else {
          // center anchors default to right, fallback to left if no room
          targetX = x + mainWidth + subWidth;
          if (targetX + subWidth > Screen.getPrimary().getBounds().getMaxX()) {
            targetX = x - subWidth;
          }
        }
      }

      subStage.setX(targetX);
      subStage.setY(targetY);
    });
  }

  public static void openLink(String url) {
    if (Desktop.isDesktopSupported()) {
      new Thread(() -> {
        try {
          Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
          e.printStackTrace();
        }
      }).start();
    } else {
      System.err.println("Desktop browsing not supported.");
    }
  }

}