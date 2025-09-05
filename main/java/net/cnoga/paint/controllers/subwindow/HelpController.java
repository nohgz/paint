package net.cnoga.paint.controllers.subwindow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * JavaFX controller for the "Help" dialog.
 *
 * <p>Loads and displays the help documentation from
 * {@code /net/cnoga/paint/text/HelpText.txt} into the UI text area.</p>
 *
 * <p>This controller is automatically initialized by the FXML loader
 * when the "Help" dialog view is created.</p>
 *
 * @author cnoga
 * @version 1.0
 */
public class HelpController {
  @FXML
  public TextArea helpTextArea;

  @FXML
  public void initialize() throws IOException {
    helpTextArea.setText(
      Files.readString(Path.of("src/main/resources/net/cnoga/paint/text/HelpText.txt"))
    );
  }
}
