package net.cnoga.paint.services;

import javafx.stage.Stage;

public class ProgramIOService {

  private final Stage primaryStage;

  public ProgramIOService(Stage stage) {
    this.primaryStage = stage;
  }

  public void closeApp() {
    primaryStage.close();
  }
}
