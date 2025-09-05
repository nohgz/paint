package net.cnoga.paint.controllers.subwindow;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.events.response.ToolChangedEvent;
import net.cnoga.paint.tool.Tool;
import net.cnoga.paint.tool.ToolRegistry;

public class ToolsController extends EventBusPublisher {

  @FXML
  private GridPane toolGrid;
  private final ToggleGroup toolGroup = new ToggleGroup();

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
    int col = 0, row = 0;

    for (Tool tool : ToolRegistry.getAll().values()) {
      ToggleButton btn = new ToggleButton();

      ImageView icon = new ImageView(tool.getIconPath());
      icon.setFitHeight(20);
      icon.setFitWidth(20);

      btn.setGraphic(icon);
      btn.setToggleGroup(toolGroup);

      btn.setOnAction(e -> {
        if (btn.isSelected()) {
          bus.post(new ToolChangedEvent(tool));
        }
      });

      toolGrid.add(btn, col, row);
      col++;
      if (col >= 2) {
        col = 0;
        row++;
      }
    }
  }
}
