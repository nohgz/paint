package net.cnoga.paint.core.util;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Utility class for creating and positioning subwindows (secondary {@link Stage} instances)
 * relative to a main JavaFX application window.
 * <p>
 * Provides methods to:
 * <ul>
 *   <li>Create simple subwindows from FXML</li>
 *   <li>Create toggle-linked subwindows that sync with a {@link ToggleButton}</li>
 *   <li>Anchor subwindows at relative positions on the main stage</li>
 * </ul>
 */
public final class SubwindowUtil {

  private SubwindowUtil() {
    // Utility class: prevent instantiation
  }

  /**
   * Creates a new subwindow from an FXML file.
   *
   * @param title     title of the subwindow
   * @param fxmlPath  classpath-relative path to the FXML file
   * @param mainStage the owning stage (used for modality and positioning)
   * @param resizable whether the window should be resizable
   * @param x         initial x-coordinate
   * @param y         initial y-coordinate
   * @return the created {@link Stage}
   * @throws IllegalArgumentException if the FXML resource is not found
   * @throws RuntimeException         if the FXML fails to load
   */
  public static Stage createSubwindow(
    String title,
    String fxmlPath,
    Stage mainStage,
    boolean resizable,
    double x,
    double y
  ) {
    URL fxmlUrl = SubwindowUtil.class.getResource(fxmlPath);
    if (fxmlUrl == null) {
      throw new IllegalArgumentException("FXML not found at path: " + fxmlPath);
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
      mainStage.focusedProperty().addListener((obs, was, is) -> sub.setAlwaysOnTop(is));

      return sub;
    } catch (IOException e) {
      throw new RuntimeException("Failed to load FXML at path: " + fxmlPath, e);
    }
  }

  /**
   * Creates a new subwindow tied to a {@link ToggleButton}. When the subwindow is closed manually,
   * the toggle button will be fired to keep its state consistent.
   *
   * @param title        title of the subwindow
   * @param fxmlPath     classpath-relative path to the FXML file
   * @param mainStage    the owning stage
   * @param resizable    whether the window should be resizable
   * @param x            initial x-coordinate
   * @param y            initial y-coordinate
   * @param toggleButton the toggle button associated with the window
   * @return the created {@link Stage}
   */
  public static Stage createToggledSubwindow(
    String title,
    String fxmlPath,
    Stage mainStage,
    boolean resizable,
    double x,
    double y,
    ToggleButton toggleButton
  ) {
    Stage subwindow = createSubwindow(title, fxmlPath, mainStage, resizable, x, y);

    subwindow.setOnCloseRequest(event -> {
      if (toggleButton != null) {
        toggleButton.fire();
      }
    });

    return subwindow;
  }

  /**
   * Positions a subwindow relative to a main window using an anchor.
   *
   * @param subStage   the subwindow to position
   * @param mainStage  the main stage to anchor against
   * @param anchorType where to anchor the subwindow
   */
  public static void setSubwindowSpawnPoint(Stage subStage, Stage mainStage,
    AnchorTypes anchorType) {
    subStage.setOnShown(e -> {
      double x = mainStage.getX();
      double y = mainStage.getY() + 110; // offset for toolbars/menus

      double mainWidth = mainStage.getWidth();
      double mainHeight = mainStage.getHeight() - 160;
      double subWidth = subStage.getWidth();
      double subHeight = subStage.getHeight();

      double targetX = calculateX(x, mainWidth, subWidth, anchorType);
      double targetY = calculateY(y, mainHeight, subHeight, anchorType);

      // If subwindow is wider than main, push it outside to avoid overlap
      if (subWidth > mainWidth) {
        targetX = resolveOversizedX(x, mainWidth, subWidth, anchorType);
      }

      subStage.setX(targetX);
      subStage.setY(targetY);
    });
  }

  private static double calculateX(double baseX, double mainWidth, double subWidth,
    AnchorTypes anchor) {
    switch (anchor) {
      case TOP_CENTER:
      case MIDDLE_CENTER:
      case BOTTOM_CENTER:
        return baseX + (mainWidth - subWidth) / 2;
      case TOP_RIGHT:
      case MIDDLE_RIGHT:
      case BOTTOM_RIGHT:
        return baseX + mainWidth - subWidth;
      default: // LEFT anchors
        return baseX;
    }
  }

  private static double calculateY(double baseY, double mainHeight, double subHeight,
    AnchorTypes anchor) {
    switch (anchor) {
      case MIDDLE_LEFT:
      case MIDDLE_CENTER:
      case MIDDLE_RIGHT:
        return baseY + (mainHeight - subHeight) / 2;
      case BOTTOM_LEFT:
      case BOTTOM_CENTER:
      case BOTTOM_RIGHT:
        return baseY + mainHeight - subHeight;
      default: // TOP anchors
        return baseY;
    }
  }

  private static double resolveOversizedX(double baseX, double mainWidth, double subWidth,
    AnchorTypes anchor) {
    switch (anchor) {
      case TOP_LEFT:
      case MIDDLE_LEFT:
      case BOTTOM_LEFT:
        return baseX - subWidth;
      case TOP_RIGHT:
      case MIDDLE_RIGHT:
      case BOTTOM_RIGHT:
      case TOP_CENTER:
      case MIDDLE_CENTER:
      case BOTTOM_CENTER:
      default:
        double candidate = baseX + mainWidth + subWidth;
        double screenMaxX = Screen.getPrimary().getBounds().getMaxX();
        return (candidate + subWidth > screenMaxX) ? baseX - subWidth : candidate;
    }
  }
}
