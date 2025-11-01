package net.cnoga.paint.core.fxml_controllers.subwindow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * JavaFX controller for the "Changelog" dialog.
 *
 * <p>Loads and displays the release changelog from
 * {@code /net/cnoga/paint/text/ChangelogText.txt} into the UI text area.</p>
 *
 * <p>This controller is automatically initialized by the FXML loader
 * when the "Changelog" dialog view is created.</p>
 *
 *
 *
 */
public class ChangelogController {

  @FXML
  private TextArea changelogTextArea;

  @FXML
  public void initialize() throws IOException {
    changelogTextArea.setText(
      Files.readString(Path.of("src/main/resources/net/cnoga/paint/text/ChangelogText.txt"))
    );
  }
}
