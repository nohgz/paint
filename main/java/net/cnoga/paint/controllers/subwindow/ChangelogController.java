package net.cnoga.paint.controllers.subwindow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

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
