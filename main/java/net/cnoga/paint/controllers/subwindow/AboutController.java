package net.cnoga.paint.controllers.subwindow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * JavaFX controller for the "About" dialog.
 *
 * <p>Loads and displays static information about the application from
 * {@code /net/cnoga/paint/text/AboutText.txt} into the UI text area.</p>
 *
 * <p>This controller is automatically initialized by the FXML loader
 * when the "About" dialog view is created.</p>
 *
 * @author cnoga
 * @version 1.0
 */
public class AboutController {

  @FXML
  public TextArea aboutTextArea;

  @FXML
  public void initialize() throws IOException {
    aboutTextArea.setText(
      Files.readString(Path.of("src/main/resources/net/cnoga/paint/text/AboutText.txt"))
    );
  }
}
