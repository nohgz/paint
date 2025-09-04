package net.cnoga.paint.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class ToolsController {

  @FXML
  private GridPane toolGrid;

  private ToggleGroup toolGroup = new ToggleGroup();

  @FXML
  public void initialize() {
    setupGrid();
    populateTools();
  }

  private void setupGrid() {
    // Two equal-width columns
    ColumnConstraints col1 = new ColumnConstraints();
    col1.setPercentWidth(50);
    ColumnConstraints col2 = new ColumnConstraints();
    col2.setPercentWidth(50);
    toolGrid.getColumnConstraints().addAll(col1, col2);

    // Six equal-height rows
    for (int i = 0; i < 6; i++) {
      RowConstraints row = new RowConstraints();
      row.setPercentHeight(100.0 / 6);
      toolGrid.getRowConstraints().add(row);
    }
  }


  private void populateTools() {
//
//    for (Tool tool : ToolRegistry.getTools()) {
//      ToggleButton button = new ToggleButton();
//      button.setGraphic(new ImageView(new Image(tool.getIconPath())));
//      button.setTooltip(new Tooltip(tool.getName()));
//
//      button.setOnAction(e -> currentTool = tool); // set current tool
//
//      toolsGridPane.getChildren().add(button);
//      // optionally assign to a ToggleGroup
//    }

    for (int row = 0; row < 6; row++) {
      for (int col = 0; col < 2; col++) {
        ToggleButton btn = new ToggleButton("T " + row + "," + col);
        btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // fill cell
        btn.setToggleGroup(toolGroup);
        toolGrid.add(btn, col, row);
      }
    }
  }
}
